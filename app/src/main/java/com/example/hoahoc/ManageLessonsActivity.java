package com.example.hoahoc;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.adapter.LessonAdapter;
import com.example.hoahoc.model.Lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageLessonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LessonAdapter adapter;
    private List<Lesson> lessonList;

    private DatabaseHelper dbHelper;
    private int nextId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_lessons);

        recyclerView = findViewById(R.id.recyclerLessons);
        findViewById(R.id.btnAddLesson).setOnClickListener(view -> showLessonDialog(null));

        lessonList = new ArrayList<>();
        adapter = new LessonAdapter(lessonList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        dbHelper = new DatabaseHelper(this);
        lessonList = dbHelper.getAllLessons();
        adapter.updateData(lessonList);
        adapter.setAdminMode(true); // Chế độ quản trị viên


        adapter.setOnItemActionListener(new LessonAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Lesson lesson) {
                showLessonDialog(lesson); // Hiển thị dialog sửa
            }

            @Override
            public void onDelete(Lesson lesson) {
                new AlertDialog.Builder(ManageLessonsActivity.this)
                        .setTitle("Xóa bài giảng")
                        .setMessage("Bạn có chắc chắn muốn xóa bài giảng này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteLesson(lesson.getId()); // Xóa khỏi SQLite
                            lessonList.remove(lesson);             // Xóa khỏi danh sách hiển thị
                            adapter.notifyDataSetChanged();        // Cập nhật giao diện
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });


    }

    private void showLessonDialog(@Nullable Lesson lesson) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_or_edit_lesson, null);
        builder.setView(view);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        EditText edtTenchuong = view.findViewById(R.id.edtTenchuong);
        Spinner spinnerLop = view.findViewById(R.id.spinnerLop);
        EditText edtThongtin = view.findViewById(R.id.edtThongtin);
        Button btnLuu = view.findViewById(R.id.btnLuu);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        AlertDialog dialog = builder.create();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Lớp 8", "Lớp 9"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(spinnerAdapter);


        if (lesson != null) {
            tvTitle.setText("Sửa bài giảng");
            edtTenchuong.setText(lesson.getTenchuong());
            edtThongtin.setText(lesson.getThongtin());
            int index = lesson.getLop().equals("Lớp 8") ? 0 : 1;
            spinnerLop.setSelection(index);
        } else {
            tvTitle.setText("Thêm bài giảng");
        }

        btnLuu.setOnClickListener(v -> {
            String tenchuong = edtTenchuong.getText().toString().trim();
            String lop = spinnerLop.getSelectedItem().toString();
            String thongtin = edtThongtin.getText().toString().trim();

            if (tenchuong.isEmpty()) {
                edtTenchuong.setError("Tên chương không được để trống");
                return;
            }

            if (lesson == null) {
                int id = dbHelper.getNextLessonId();
                Lesson newLesson = new Lesson(String.valueOf(id), tenchuong, lop, thongtin);
                dbHelper.insertLesson(newLesson);
            } else {
                lesson.setTenchuong(tenchuong);
                lesson.setLop(lop);
                lesson.setThongtin(thongtin);
                dbHelper.updateLesson(lesson);
            }

            // Load lại danh sách từ DB và cập nhật adapter
            lessonList = dbHelper.getAllLessons();
            adapter.updateData(lessonList);

            dialog.dismiss();
        });


        btnHuy.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
