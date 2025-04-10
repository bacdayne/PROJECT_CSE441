package com.example.hoahoc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hoahoc.model.Lesson;
import com.example.hoahoc.model.Lesson;

public class LectureDetailsActivity extends AppCompatActivity {
    TextView txtchitietbaigiang, txttenchuong;
    private Toolbar toolbar_chitietbaigiang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecture_details);


        txtchitietbaigiang = findViewById(R.id.ly_thuyet_chuong);
        txttenchuong = findViewById(R.id.tv_tenchuong);
        toolbar_chitietbaigiang = findViewById(R.id.toolbar_chitietbaigiang);

        // Nhận dữ liệu
        Intent intent = getIntent();
        Lesson chuong = (Lesson) intent.getSerializableExtra("baigiang");
//        Lesson chuong2 = (Lesson) intent.getSerializableExtra("baigiang");
//        Lesson chuong3 = (Lesson) intent.getSerializableExtra("baigiang");
//        Lesson chuong4 = (Lesson) intent.getSerializableExtra("baigiang");
//        Lesson chuong5 = (Lesson) intent.getSerializableExtra("baigiang");
//        Lesson chuong6 = (Lesson) intent.getSerializableExtra("baigiang");


        // Hiển thị nội dung
        txtchitietbaigiang.setText(Html.fromHtml(chuong.getThongtin()));
//        txtchitietbaigiang.setText(Html.fromHtml(chuong2.getThongtin()));
//        txtchitietbaigiang.setText(Html.fromHtml(chuong3.getThongtin()));
//        txtchitietbaigiang.setText(Html.fromHtml(chuong4.getThongtin()));
//        txtchitietbaigiang.setText(Html.fromHtml(chuong5.getThongtin()));
//        txtchitietbaigiang.setText(Html.fromHtml(chuong6.getThongtin()));

        txttenchuong.setText(chuong.getTenchuong());
//        txttenchuong.setText(chuong2.getTenchuong());

        // Set Toolbar
        setSupportActionBar(toolbar_chitietbaigiang);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(chuong.getTenchuong()); // nếu muốn hiện ở Toolbar
        }

        toolbar_chitietbaigiang.setNavigationOnClickListener(v -> onBackPressed());

        ImageView iconSave = findViewById(R.id.icon_save);
        DatabaseHelper help = new DatabaseHelper(this);


        if (chuong.isSaved()) {
            iconSave.setImageResource(R.drawable.save);
        } else {
            iconSave.setImageResource(R.drawable.not_save);
        }


        iconSave.setOnClickListener(v -> {
            boolean newStatus = !chuong.isSaved();
            help.updateSavedStatus(chuong.getId(), newStatus);
            chuong.setSaved(newStatus);

            // Đổi icon ngay
            if (newStatus) {
                iconSave.setImageResource(R.drawable.save);
            } else {
                iconSave.setImageResource(R.drawable.not_save);
            }
        });

    }
}