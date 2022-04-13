package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordsPage extends AppCompatActivity {

    EditText new_passwords;
    Button confirm_change_btn,to_login_btn;

    private FirebaseAuth verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwords_page);
        new_passwords = findViewById(R.id.forgot_pasword_new_password_txt);

        verify = FirebaseAuth.getInstance();

        confirm_change_btn = findViewById(R.id.new_password_btn);
        confirm_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPass();
            }
        });

        to_login_btn = findViewById(R.id.back_to_loginpage);

        to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordsPage.this,LoginPage.class));
            }
        });


    }

    private void resetPass() {

        String email = new_passwords.getText().toString();

        if(email.isEmpty() ){
            new_passwords.setError("Email không được trống");
            new_passwords.requestFocus();
            return;}
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            new_passwords.setError("Email phải hợp lệ");
            new_passwords.requestFocus();
            return;
        }
        else{
            verify.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordsPage.this, "Xác nhận thay đổi, vui lòng kiểm tra email cá nhân", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ForgotPasswordsPage.this, "Đã phát hiện lỗi!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}