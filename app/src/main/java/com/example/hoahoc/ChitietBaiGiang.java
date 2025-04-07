package com.example.hoahoc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hoahoc.model.baigiang;

public class ChitietBaiGiang extends AppCompatActivity {
    TextView txtchitietbaigiang;
    private Toolbar toolbar_chitietbaigiang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitiet_bai_giang);

        // anh xa
        txtchitietbaigiang = findViewById(R.id.ly_thuyet_chuong);
        Intent intent = getIntent();
        baigiang chuong1 = (baigiang) intent.getSerializableExtra("baigiang");

        txtchitietbaigiang.setText(Html.fromHtml(chuong1.getThongtin()));
        toolbar_chitietbaigiang = findViewById(R.id.toolbar_chitietbaigiang);
        setSupportActionBar(toolbar_chitietbaigiang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_chitietbaigiang.setNavigationOnClickListener(v -> onBackPressed());


    }
}