package com.example.hoahoc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hoahoc.model.baigiang;

public class ChitietBaiGiang extends AppCompatActivity {
    TextView txtchitietbaigiang, txttenchuong;
    private Toolbar toolbar_chitietbaigiang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitiet_bai_giang);


        txtchitietbaigiang = findViewById(R.id.ly_thuyet_chuong);
        txttenchuong = findViewById(R.id.tv_tenchuong);
        toolbar_chitietbaigiang = findViewById(R.id.toolbar_chitietbaigiang);

        // Nhận dữ liệu
        Intent intent = getIntent();
        baigiang chuong1 = (baigiang) intent.getSerializableExtra("baigiang");
        baigiang chuong2 = (baigiang) intent.getSerializableExtra("baigiang");
        baigiang chuong3 = (baigiang) intent.getSerializableExtra("baigiang");
        baigiang chuong4 = (baigiang) intent.getSerializableExtra("baigiang");
        baigiang chuong5 = (baigiang) intent.getSerializableExtra("baigiang");
        baigiang chuong6 = (baigiang) intent.getSerializableExtra("baigiang");


        // Hiển thị nội dung
        txtchitietbaigiang.setText(Html.fromHtml(chuong1.getThongtin()));
        txtchitietbaigiang.setText(Html.fromHtml(chuong2.getThongtin()));
        txtchitietbaigiang.setText(Html.fromHtml(chuong3.getThongtin()));
        txtchitietbaigiang.setText(Html.fromHtml(chuong4.getThongtin()));
        txtchitietbaigiang.setText(Html.fromHtml(chuong5.getThongtin()));
        txtchitietbaigiang.setText(Html.fromHtml(chuong6.getThongtin()));

        txttenchuong.setText(chuong1.getTenchuong());
        txttenchuong.setText(chuong2.getTenchuong());

        // Set Toolbar
        setSupportActionBar(toolbar_chitietbaigiang);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(chuong1.getTenchuong()); // nếu muốn hiện ở Toolbar
        }

        toolbar_chitietbaigiang.setNavigationOnClickListener(v -> onBackPressed());
    }
}
