package com.example.hoahoc.view.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hoahoc.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            if (!validateEmail() | !validatePassword()) {
                return;
            }
            loginUser();
        });

        signupRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private boolean validateEmail() {
        String email = loginEmail.getText().toString();
        if (email.isEmpty()) {
            loginEmail.setError("Bạn phải nhập email!");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String pass = loginPassword.getText().toString();
        if (pass.isEmpty()) {
            loginPassword.setError("Bạn phải nhập mật khẩu!");
            return false;
        }
        return true;
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
