package com.example.hoahoc.view.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.controller.DatabaseHelper;
import com.example.hoahoc.R;
import com.example.hoahoc.view.view.adapter.QuestionAdapter;
import com.example.hoahoc.model.Question;

import java.util.ArrayList;
import java.util.List;

public class ManageQuestionsActivity extends AppCompatActivity {

    private TextView tvManageQuestionsTitle;
    private RecyclerView recyclerQuestions;
    private Button btnAddQuestion;
    private DatabaseHelper dbHelper;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        tvManageQuestionsTitle = findViewById(R.id.tvManageQuestionsTitle);
        recyclerQuestions = findViewById(R.id.recyclerQuestions);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        dbHelper = new DatabaseHelper(this);
        questionList = new ArrayList<>();

        loadQuestions();

        questionAdapter = new QuestionAdapter(questionList, new QuestionAdapter.OnQuestionActionListener() {
            @Override
            public void onEdit(Question question) {
                showAddEditQuestionDialog(question, true);
            }

            @Override
            public void onDelete(Question question) {
                new AlertDialog.Builder(ManageQuestionsActivity.this)
                        .setTitle("Xóa câu hỏi")
                        .setMessage("Bạn có chắc chắn muốn xóa câu hỏi này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteQuestion(question.getId());
                            questionList.remove(question);
                            questionAdapter.notifyDataSetChanged();
                            Toast.makeText(ManageQuestionsActivity.this, "Xóa câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        recyclerQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuestions.setAdapter(questionAdapter);

        btnAddQuestion.setOnClickListener(v -> showAddEditQuestionDialog(null, false));
    }

    private void loadQuestions() {
        questionList.clear();
        // Lấy tất cả câu hỏi (có thể thêm bộ lọc theo examId nếu cần)
        for (int examId = 1; examId <= 10; examId++) { // Giả sử có 10 đề
            questionList.addAll(dbHelper.getQuestionsByExamIdList(examId));
        }
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Chưa có câu hỏi nào!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddEditQuestionDialog(Question question, boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_or_edit_question, null);
        builder.setView(dialogView);

        TextView tvDialogTitle = dialogView.findViewById(R.id.tvTitle);
        Spinner spinnerExamId = dialogView.findViewById(R.id.spinnerExamId);
        EditText edtQuestionText = dialogView.findViewById(R.id.edtQuestionText);
        EditText edtOptionA = dialogView.findViewById(R.id.edtOptionA);
        EditText edtOptionB = dialogView.findViewById(R.id.edtOptionB);
        EditText edtOptionC = dialogView.findViewById(R.id.edtOptionC);
        EditText edtOptionD = dialogView.findViewById(R.id.edtOptionD);
        EditText edtCorrectAnswer = dialogView.findViewById(R.id.edtCorrectAnswer);
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        Button btnLuu = dialogView.findViewById(R.id.btnLuu);

        // Thiết lập Spinner cho examId
        List<String> examIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) { // Giả sử có 10 đề
            examIds.add(String.valueOf(i));
        }
        ArrayAdapter<String> examAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, examIds);
        examAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamId.setAdapter(examAdapter);

        if (isEdit && question != null) {
            tvDialogTitle.setText("Sửa câu hỏi");
            spinnerExamId.setSelection(examIds.indexOf(String.valueOf(question.getExamId())));
            edtQuestionText.setText(question.getQuestionText());
            edtOptionA.setText(question.getOptionA());
            edtOptionB.setText(question.getOptionB());
            edtOptionC.setText(question.getOptionC());
            edtOptionD.setText(question.getOptionD());
            edtCorrectAnswer.setText(question.getCorrectAnswer());
        } else {
            tvDialogTitle.setText("Thêm câu hỏi");
        }

        AlertDialog dialog = builder.create();

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLuu.setOnClickListener(v -> {
            String examIdStr = spinnerExamId.getSelectedItem().toString();
            String questionText = edtQuestionText.getText().toString().trim();
            String optionA = edtOptionA.getText().toString().trim();
            String optionB = edtOptionB.getText().toString().trim();
            String optionC = edtOptionC.getText().toString().trim();
            String optionD = edtOptionD.getText().toString().trim();
            String correctAnswer = edtCorrectAnswer.getText().toString().trim();

            if (examIdStr.isEmpty() || questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                    optionC.isEmpty() || optionD.isEmpty() || correctAnswer.isEmpty()) {
                Toast.makeText(ManageQuestionsActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C") && !correctAnswer.equals("D")) {
                Toast.makeText(ManageQuestionsActivity.this, "Đáp án đúng phải là A, B, C hoặc D!", Toast.LENGTH_SHORT).show();
                return;
            }

            int examId = Integer.parseInt(examIdStr);
            Question newQuestion = new Question(
                    isEdit ? question.getId() : 0,
                    examId,
                    questionText,
                    optionA,
                    optionB,
                    optionC,
                    optionD,
                    correctAnswer
            );

            if (isEdit) {
                int result = dbHelper.updateQuestion(newQuestion);
                if (result > 0) {
                    Toast.makeText(ManageQuestionsActivity.this, "Sửa câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                    loadQuestions();
                    questionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageQuestionsActivity.this, "Sửa câu hỏi thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                long result = dbHelper.addQuestion(newQuestion);
                if (result != -1) {
                    Toast.makeText(ManageQuestionsActivity.this, "Thêm câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                    loadQuestions();
                    questionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageQuestionsActivity.this, "Thêm câu hỏi thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}