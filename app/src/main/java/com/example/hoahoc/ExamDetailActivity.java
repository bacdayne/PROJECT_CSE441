package com.example.hoahoc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hoahoc.model.ExamHistory;
import com.example.hoahoc.model.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ExamDetailActivity extends AppCompatActivity {

    private TextView tvExamTitle, tvQuestionCount, tvTimer, tvQuestion;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private Button btnSelectA, btnSelectB, btnSelectC, btnSelectD, btnPrevious, btnStop, btnNext;
    private DatabaseHelper dbHelper;
    private List<Question> questionList;
    private List<Question> shuffledQuestionList;
    private int currentQuestionIndex = 0;
    private int examId;
    private CountDownTimer countDownTimer;
    private List<String> userAnswers;
    private List<Boolean> answeredStatus;
    private int totalCorrect = 0;
    private int totalAnswered = 0;
    private static final int QUESTIONS_PER_EXAM = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);

        tvExamTitle = findViewById(R.id.tvExamTitle);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        btnSelectA = findViewById(R.id.btnSelectA);
        btnSelectB = findViewById(R.id.btnSelectB);
        btnSelectC = findViewById(R.id.btnSelectC);
        btnSelectD = findViewById(R.id.btnSelectD);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnStop = findViewById(R.id.btnStop);
        btnNext = findViewById(R.id.btnNext);
        dbHelper = new DatabaseHelper(this);
        questionList = new ArrayList<>();
        shuffledQuestionList = new ArrayList<>();
        userAnswers = new ArrayList<>();
        answeredStatus = new ArrayList<>();

        examId = getIntent().getIntExtra("EXAM_ID", 1);
        tvExamTitle.setText("ĐỀ SỐ " + examId);
        loadQuestions();
        generateExam();

        for (int i = 0; i < shuffledQuestionList.size(); i++) {
            userAnswers.add(null);
            answeredStatus.add(false);
        }

        displayQuestion();
        startTimer(30 * 60 * 1000);

        btnSelectA.setOnClickListener(v -> checkAnswer("A"));
        btnSelectB.setOnClickListener(v -> checkAnswer("B"));
        btnSelectC.setOnClickListener(v -> checkAnswer("C"));
        btnSelectD.setOnClickListener(v -> checkAnswer("D"));

        btnPrevious.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < shuffledQuestionList.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            }
        });

        btnStop.setOnClickListener(v -> showSubmitDialog());
    }

    private void loadQuestions() {
        Cursor cursor = dbHelper.getQuestionsByExamId(examId);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int examId = cursor.getInt(cursor.getColumnIndex("exam_id"));
                @SuppressLint("Range") String questionText = cursor.getString(cursor.getColumnIndex("question_text"));
                @SuppressLint("Range") String optionA = cursor.getString(cursor.getColumnIndex("option_a"));
                @SuppressLint("Range") String optionB = cursor.getString(cursor.getColumnIndex("option_b"));
                @SuppressLint("Range") String optionC = cursor.getString(cursor.getColumnIndex("option_c"));
                @SuppressLint("Range") String optionD = cursor.getString(cursor.getColumnIndex("option_d"));
                @SuppressLint("Range") String correctAnswer = cursor.getString(cursor.getColumnIndex("correct_answer"));
                questionList.add(new Question(id, examId, questionText, optionA, optionB, optionC, optionD, correctAnswer));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void generateExam() {
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào cho đề này!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<Question> tempList = new ArrayList<>(questionList);
        int questionsToSelect = Math.min(QUESTIONS_PER_EXAM, tempList.size());
        Collections.shuffle(tempList, new Random());
        shuffledQuestionList.clear();
        for (int i = 0; i < questionsToSelect; i++) {
            Question question = tempList.get(i);
            question = shuffleOptions(question);
            shuffledQuestionList.add(question);
        }
        tvQuestionCount.setText(shuffledQuestionList.size() + " câu");
    }

    private Question shuffleOptions(Question question) {
        List<String> options = new ArrayList<>();
        options.add(question.getOptionA());
        options.add(question.getOptionB());
        options.add(question.getOptionC());
        options.add(question.getOptionD());

        String correctAnswer = question.getCorrectAnswer();
        String correctOption = "";
        switch (correctAnswer) {
            case "A":
                correctOption = question.getOptionA();
                break;
            case "B":
                correctOption = question.getOptionB();
                break;
            case "C":
                correctOption = question.getOptionC();
                break;
            case "D":
                correctOption = question.getOptionD();
                break;
            default:
                // Nếu correctAnswer không hợp lệ, báo lỗi và trả về câu hỏi gốc
                Toast.makeText(this, "Dữ liệu câu hỏi không hợp lệ: Đáp án đúng không xác định!", Toast.LENGTH_SHORT).show();
                return question;
        }

        Collections.shuffle(options, new Random());
        String newCorrectAnswer = "";
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(correctOption)) {
                switch (i) {
                    case 0:
                        newCorrectAnswer = "A";
                        break;
                    case 1:
                        newCorrectAnswer = "B";
                        break;
                    case 2:
                        newCorrectAnswer = "C";
                        break;
                    case 3:
                        newCorrectAnswer = "D";
                        break;
                }
                break; // Thoát vòng lặp sau khi tìm thấy
            }
        }

        // Kiểm tra nếu không tìm thấy correctOption
        if (newCorrectAnswer.isEmpty()) {
            Toast.makeText(this, "Lỗi xáo trộn đáp án: Không tìm thấy đáp án đúng!", Toast.LENGTH_SHORT).show();
            return question; // Trả về câu hỏi gốc nếu có lỗi
        }

        return new Question(
                question.getId(),
                question.getExamId(),
                question.getQuestionText(),
                options.get(0),
                options.get(1),
                options.get(2),
                options.get(3),
                newCorrectAnswer
        );
    }

    private void displayQuestion() {
        if (shuffledQuestionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào cho đề này!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Question question = shuffledQuestionList.get(currentQuestionIndex);
        tvQuestion.setText("Câu " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
        tvOptionA.setText("A. " + question.getOptionA());
        tvOptionB.setText("B. " + question.getOptionB());
        tvOptionC.setText("C. " + question.getOptionC());
        tvOptionD.setText("D. " + question.getOptionD());

        resetButtonColors();

        String selectedAnswer = userAnswers.get(currentQuestionIndex);
        if (selectedAnswer != null) {
            switch (selectedAnswer) {
                case "A":
                    btnSelectA.setSelected(true);
                    break;
                case "B":
                    btnSelectB.setSelected(true);
                    break;
                case "C":
                    btnSelectC.setSelected(true);
                    break;
                case "D":
                    btnSelectD.setSelected(true);
                    break;
            }
        }

        btnPrevious.setEnabled(currentQuestionIndex > 0);
        btnNext.setEnabled(currentQuestionIndex < shuffledQuestionList.size() - 1);
    }

    private void resetButtonColors() {
        btnSelectA.setSelected(false);
        btnSelectB.setSelected(false);
        btnSelectC.setSelected(false);
        btnSelectD.setSelected(false);
    }

    private void checkAnswer(String selectedAnswer) {
        Question question = shuffledQuestionList.get(currentQuestionIndex);
        String previousAnswer = userAnswers.get(currentQuestionIndex);

        if (answeredStatus.get(currentQuestionIndex)) {
            if (previousAnswer != null && previousAnswer.equals(question.getCorrectAnswer())) {
                totalCorrect--;
            }
        } else {
            totalAnswered++;
            answeredStatus.set(currentQuestionIndex, true);
        }

        userAnswers.set(currentQuestionIndex, selectedAnswer);
        resetButtonColors();

        switch (selectedAnswer) {
            case "A":
                btnSelectA.setSelected(true);
                break;
            case "B":
                btnSelectB.setSelected(true);
                break;
            case "C":
                btnSelectC.setSelected(true);
                break;
            case "D":
                btnSelectD.setSelected(true);
                break;
        }

        if (selectedAnswer.equals(question.getCorrectAnswer())) {
            totalCorrect++;
            Toast.makeText(this, "Đúng!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sai! Đáp án đúng là: " + question.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(ExamDetailActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                showResultDialog();
            }
        }.start();
    }

    private void showSubmitDialog() {
        countDownTimer.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("Xác nhận nộp bài")
                .setMessage("Bạn có chắc chắn muốn nộp bài không? Sau khi nộp, bạn không thể tiếp tục làm bài.")
                .setPositiveButton("Nộp bài", (dialog, which) -> showResultDialog())
                .setNegativeButton("Làm tiếp", (dialog, which) -> startTimer(30 * 60 * 1000 - (30 * 60 * 1000 - countDownTimerRemainingTime())))
                .setCancelable(false)
                .show();
    }

    private long countDownTimerRemainingTime() {
        String[] timeParts = tvTimer.getText().toString().split(":");
        long minutes = Long.parseLong(timeParts[0]);
        long seconds = Long.parseLong(timeParts[1]);
        return (minutes * 60 + seconds) * 1000;
    }

    private void showResultDialog() {
        double score = totalCorrect * 0.25;

        // Lưu lịch sử bài thi
        String completionTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        StringBuilder questionIds = new StringBuilder();
        for (Question question : shuffledQuestionList) {
            questionIds.append(question.getId()).append(",");
        }
        StringBuilder answers = new StringBuilder();
        for (String answer : userAnswers) {
            answers.append(answer != null ? answer : "N/A").append(",");
        }
        ExamHistory history = new ExamHistory(
                0, // historyId sẽ được tự động tạo
                examId,
                completionTime,
                totalCorrect,
                score,
                questionIds.toString(),
                answers.toString()
        );
        long result = dbHelper.saveExamHistory(history);
        if (result != -1) {
            Toast.makeText(this, "Lịch sử bài thi đã được lưu!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi lưu lịch sử bài thi!", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("Kết quả bài thi")
                .setMessage("Bạn đã hoàn thành bài thi!\n\nSố câu hoàn thành: " + totalAnswered + "\nSố câu đúng: " + totalCorrect + "\nĐiểm số: " + score)
                .setPositiveButton("Xem chi tiết", (dialog, which) -> {
                    Intent detailIntent = new Intent(ExamDetailActivity.this, ResultDetailActivity.class);
                    detailIntent.putExtra("QUESTION_LIST", new ArrayList<>(shuffledQuestionList));
                    detailIntent.putStringArrayListExtra("USER_ANSWERS", new ArrayList<>(userAnswers));
                    startActivity(detailIntent);
                    finish();
                })
                .setNegativeButton("Thoát", (dialog, which) -> finish())
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}