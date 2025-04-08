package com.example.hoahoc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;

import com.example.hoahoc.adapter.ExamAdapter;
import com.example.hoahoc.model.Exam;

import java.util.ArrayList;
import java.util.List;

public class TracNghiemActivity extends AppCompatActivity{
    private RecyclerView recyclerViewExams;
    private ExamAdapter examAdapter;
    private List<Exam> examList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracnghiem);

        Button btnImportCSV = findViewById(R.id.btnImportCSV);
        recyclerViewExams = findViewById(R.id.recyclerViewExams);
        dbHelper = new DatabaseHelper(this);
        examList = new ArrayList<>();

        recyclerViewExams.setLayoutManager(new GridLayoutManager(this, 3));
        loadExams();

        btnImportCSV.setOnClickListener(v -> {
            dbHelper.importQuestionsFromCSV();
            Toast.makeText(this, "Đã import câu hỏi từ CSV!", Toast.LENGTH_SHORT).show();
            loadExams(); // Cập nhật lại danh sách nếu cần
        });
    }

    private void loadExams() {
        Cursor cursor = dbHelper.getAllExams();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int examNumber = cursor.getInt(cursor.getColumnIndex("exam_number"));
                examList.add(new Exam(id, examNumber));
            } while (cursor.moveToNext());
        }
        cursor.close();
        examAdapter = new ExamAdapter(this, examList);
        recyclerViewExams.setAdapter(examAdapter);
    }
}
