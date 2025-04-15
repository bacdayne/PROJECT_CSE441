package com.example.hoahoc.view.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hoahoc.controller.DatabaseHelper;
import com.example.hoahoc.R;
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

        // Kiểm tra chuong có null hay không
        if (chuong != null) {
            // Kiểm tra các trường trong chuong
            if (chuong.getThongtin() != null) {
                txtchitietbaigiang.setText(Html.fromHtml(chuong.getThongtin()));
            } else {
                txtchitietbaigiang.setText("Thông tin không có sẵn"); // Hoặc một giá trị mặc định
            }

            if (chuong.getTenchuong() != null) {
                txttenchuong.setText(chuong.getTenchuong());
            } else {
                txttenchuong.setText("Chưa có tên chương"); // Hoặc một giá trị mặc định
            }

            // Set Toolbar
            setSupportActionBar(toolbar_chitietbaigiang);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(chuong.getTenchuong()); // Nếu muốn hiện ở Toolbar
            }

            toolbar_chitietbaigiang.setNavigationOnClickListener(v -> onBackPressed());

            ImageView iconSave = findViewById(R.id.icon_save);
            DatabaseHelper help = new DatabaseHelper(this);

            // Kiểm tra trạng thái lưu
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
        } else {
            // Xử lý khi chuong là null
            txtchitietbaigiang.setText("Dữ liệu không hợp lệ");
            txttenchuong.setText("Chương không tồn tại");
        }
    }

}