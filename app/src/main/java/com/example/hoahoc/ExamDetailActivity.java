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

import com.example.hoahoc.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExamDetailActivity extends AppCompatActivity {

    private TextView tvExamTitle, tvQuestionCount, tvTimer, tvQuestion;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private Button btnSelectA, btnSelectB, btnSelectC, btnSelectD, btnPrevious, btnStop, btnNext;
    private DatabaseHelper dbHelper;
    private List<Question> questionList; // Danh sách câu hỏi gốc
    private List<Question> shuffledQuestionList; // Danh sách câu hỏi đã đảo
    private int currentQuestionIndex = 0;
    private int examId;
    private CountDownTimer countDownTimer;
    private List<String> userAnswers; // Lưu đáp án người dùng chọn
    private List<Boolean> answeredStatus; // Theo dõi trạng thái đã trả lời của từng câu
    private int totalCorrect = 0; // Số câu trả lời đúng
    private int totalAnswered = 0; // Số câu đã trả lời
    private static final int QUESTIONS_PER_EXAM = 40; // Số câu hỏi mỗi đề (có thể thay đổi)

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

        // Khởi tạo danh sách đáp án người dùng và trạng thái trả lời
        examId = getIntent().getIntExtra("EXAM_ID", 1);
        tvExamTitle.setText("ĐỀ SỐ " + examId);
        loadQuestions();
        generateExam(); // Sinh đề mới hoặc đảo thứ tự câu hỏi

        for (int i = 0; i < shuffledQuestionList.size(); i++) {
            userAnswers.add(null); // Chưa trả lời
            answeredStatus.add(false); // Chưa được trả lời
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
        // Phương pháp 1: Chọn ngẫu nhiên QUESTIONS_PER_EXAM câu hỏi từ questionList
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào cho đề này!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Sao chép danh sách câu hỏi gốc
        List<Question> tempList = new ArrayList<>(questionList);

        // Nếu số câu hỏi ít hơn số câu cần cho một đề, sử dụng tất cả câu hỏi
        int questionsToSelect = Math.min(QUESTIONS_PER_EXAM, tempList.size());

        // Chọn ngẫu nhiên QUESTIONS_PER_EXAM câu hỏi
        Collections.shuffle(tempList, new Random());
        shuffledQuestionList.clear();
        for (int i = 0; i < questionsToSelect; i++) {
            Question question = tempList.get(i);
            // Đảo thứ tự đáp án
            question = shuffleOptions(question);
            shuffledQuestionList.add(question);
        }

        // Cập nhật số câu hỏi hiển thị
        tvQuestionCount.setText(shuffledQuestionList.size() + " câu");
    }

    private Question shuffleOptions(Question question) {
        // Tạo danh sách các đáp án
        List<String> options = new ArrayList<>();
        options.add(question.getOptionA());
        options.add(question.getOptionB());
        options.add(question.getOptionC());
        options.add(question.getOptionD());

        // Lưu đáp án đúng
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
        }

        // Đảo thứ tự đáp án
        Collections.shuffle(options, new Random());

        // Cập nhật lại đáp án đúng dựa trên vị trí mới
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
            }
        }

        // Tạo câu hỏi mới với đáp án đã đảo
        return new Question(
                question.getId(),
                question.getExamId(),
                question.getQuestionText(),
                options.get(0), // Option A
                options.get(1), // Option B
                options.get(2), // Option C
                options.get(3), // Option D
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

        // Đặt lại trạng thái màu của các nút
        resetButtonColors();

        // Nếu câu hỏi đã được trả lời, tô màu nút tương ứng
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
        String previousAnswer = userAnswers.get(currentQuestionIndex); // Đáp án trước đó

        // Nếu câu hỏi đã được trả lời trước đó, kiểm tra xem đáp án trước đó có đúng không
        if (answeredStatus.get(currentQuestionIndex)) {
            if (previousAnswer != null && previousAnswer.equals(question.getCorrectAnswer())) {
                totalCorrect--; // Giảm số câu đúng nếu đáp án trước đó đúng
            }
        } else {
            // Nếu đây là lần đầu trả lời câu hỏi, tăng số câu đã trả lời
            totalAnswered++;
            answeredStatus.set(currentQuestionIndex, true); // Đánh dấu câu hỏi đã được trả lời
        }

        // Lưu đáp án mới
        userAnswers.set(currentQuestionIndex, selectedAnswer);

        // Đặt lại màu của tất cả các nút trước khi tô màu nút được chọn
        resetButtonColors();

        // Tô màu nút được chọn
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

        // Kiểm tra đáp án mới
        if (selectedAnswer.equals(question.getCorrectAnswer())) {
            totalCorrect++; // Tăng số câu đúng nếu đáp án mới đúng
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
        countDownTimer.cancel(); // Tạm dừng đồng hồ
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
        // Tính điểm: mỗi câu đúng 0.25 điểm
        double score = totalCorrect * 0.25;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("Kết quả bài thi")
                .setMessage("Bạn đã hoàn thành bài thi!\n\nSố câu hoàn thành: " + totalAnswered + "\nSố câu đúng: " + totalCorrect + "\nĐiểm số: " + score)
                .setPositiveButton("Xem chi tiết", (dialog, which) -> {
                    Intent intent = new Intent(ExamDetailActivity.this, ResultDetailActivity.class);
                    intent.putExtra("QUESTION_LIST", new ArrayList<>(shuffledQuestionList));
                    intent.putStringArrayListExtra("USER_ANSWERS", new ArrayList<>(userAnswers));
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Thoát", (dialog, which) -> finish())
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info) // Thêm biểu tượng
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