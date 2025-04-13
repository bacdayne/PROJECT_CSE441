package com.example.hoahoc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.DatabaseHelper;
import com.example.hoahoc.R;
import com.example.hoahoc.ResultDetailActivity;
import com.example.hoahoc.adapter.ExamHistoryAdapter;
import com.example.hoahoc.databinding.FragmentSettingBinding;
import com.example.hoahoc.model.ExamHistory;
import com.example.hoahoc.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private RecyclerView recyclerExamHistory;
    private ExamHistoryAdapter historyAdapter;
    private DatabaseHelper dbHelper;
    private List<ExamHistory> historyList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerExamHistory = root.findViewById(R.id.recyclerExamHistory);
        dbHelper = new DatabaseHelper(requireContext());
        historyList = dbHelper.getAllExamHistory();

        historyAdapter = new ExamHistoryAdapter(historyList, new ExamHistoryAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(ExamHistory history) {
                // Xem chi tiết lịch sử bài thi
                Intent intent = new Intent(getActivity(), ResultDetailActivity.class);
                List<Question> questions = new ArrayList<>();
                List<String> userAnswers = new ArrayList<>();

                // Lấy danh sách ID câu hỏi
                String[] questionIds = history.getQuestionIds().split(",");
                String[] answers = history.getUserAnswers().split(",");

                // Lấy câu hỏi từ database
                for (String id : questionIds) {
                    if (!id.isEmpty()) {
                        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                                "SELECT * FROM questions WHERE id = ?",
                                new String[]{id}
                        );
                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range") int questionId = cursor.getInt(cursor.getColumnIndex("id"));
                            @SuppressLint("Range") int examId = cursor.getInt(cursor.getColumnIndex("exam_id"));
                            @SuppressLint("Range") String questionText = cursor.getString(cursor.getColumnIndex("question_text"));
                            @SuppressLint("Range") String optionA = cursor.getString(cursor.getColumnIndex("option_a"));
                            @SuppressLint("Range") String optionB = cursor.getString(cursor.getColumnIndex("option_b"));
                            @SuppressLint("Range") String optionC = cursor.getString(cursor.getColumnIndex("option_c"));
                            @SuppressLint("Range") String optionD = cursor.getString(cursor.getColumnIndex("option_d"));
                            @SuppressLint("Range") String correctAnswer = cursor.getString(cursor.getColumnIndex("correct_answer"));
                            questions.add(new Question(questionId, examId, questionText, optionA, optionB, optionC, optionD, correctAnswer));
                        }
                        cursor.close();
                    }
                }
                userAnswers.addAll(Arrays.asList(answers));

                intent.putExtra("QUESTION_LIST", new ArrayList<>(questions));
                intent.putStringArrayListExtra("USER_ANSWERS", new ArrayList<>(userAnswers));
                startActivity(intent);
            }

            @Override
            public void onDelete(ExamHistory history) {
                // Xóa lịch sử bài thi
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Xóa lịch sử bài thi")
                        .setMessage("Bạn có chắc chắn muốn xóa lịch sử bài thi này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteExamHistory(history.getHistoryId());
                            historyList.remove(history);
                            historyAdapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        recyclerExamHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExamHistory.setAdapter(historyAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}