package com.example.hoahoc.view.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hoahoc.R;
import com.example.hoahoc.model.Acount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Acount");

        signupButton.setOnClickListener(view -> {
            String name = signupName.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String username = signupUsername.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            // Gán role theo email đặc biệt
            String role;
            if (email.equals("admin@gmail.com")) {
                role = "admin";
            } else {
                role = "user";
            }

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();

                                Acount acount = new Acount(uid,name, email, username, password, role);
                                reference.child(uid).setValue(acount)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignupActivity.this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.e("Firebase", "Lỗi lưu dữ liệu", e);
                                        });
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Lỗi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Auth", "Đăng ký thất bại", task.getException());
                        }
                    });
        });


        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
