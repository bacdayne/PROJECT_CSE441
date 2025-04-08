package com.example.hoahoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.adapter.LessonAdapter;
import com.example.hoahoc.model.Lesson;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class LessonActivity extends AppCompatActivity {

    private Toolbar toolbar_baigiang;
    //    private SearchView search;
    private RecyclerView rcvbaigiang;
    private MaterialButton btn_lop8;
    private MaterialButton btn_lop9;
    private LessonAdapter adapterbaigiang;
    private DatabaseHelper help;
    ArrayList <Lesson> list;

    private String lop = "Lớp 8"; // Dùng biến String thay vì mảng

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson);

        toolbar_baigiang = findViewById(R.id.toolbar_baigiang);
        setSupportActionBar(toolbar_baigiang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_baigiang.setNavigationOnClickListener(v -> onBackPressed());

        rcvbaigiang = findViewById(R.id.rcv_baigianglop8);
        btn_lop8 = findViewById(R.id.btn_lop8);
        btn_lop9 = findViewById(R.id.btn_lop9);

        list = new ArrayList<>();
        adapterbaigiang = new LessonAdapter(list);

        rcvbaigiang.setAdapter(adapterbaigiang);
        rcvbaigiang.setLayoutManager(new LinearLayoutManager(this));
        help = new DatabaseHelper(this);
        loadData();

        btn_lop8.setOnClickListener(e -> {
            btn_lop8.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.pink));
            btn_lop9.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
            loadData();
        });

        adapterbaigiang.setOnItemClickListener(baigiang -> {
            Intent intent = new Intent(LessonActivity.this, LectureDetailsActivity.class);
            intent.putExtra("baigiang", baigiang);
            startActivity(intent);
        });

        btn_lop9.setOnClickListener(e -> {
            btn_lop9.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.pink));
            btn_lop8.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
            list.clear();
            list.addAll(help.getDataByLop("Lớp 9"));
            adapterbaigiang.notifyDataSetChanged();
        });
    }

    void loadData(){
        list.clear();
        list.addAll(help.getDataByLop("Lớp 8"));
        adapterbaigiang.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}