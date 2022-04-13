package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class SplashScreene2 extends AppCompatActivity {

    private FirebaseAuth db_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screene2);

        Paper.init(this);

        SharedPreferences preferences = getSharedPreferences("MyFile",MODE_PRIVATE);
        String email = preferences.getString("username","Không tìm thấy email");
        String password = preferences.getString("password","Không tìm thấy mật khẩu");

        if(email != "" && password != ""){
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                GetLogin(email,password);
            }
        }

    }

    private void GetLogin(String email, String password) {
        db_auth = FirebaseAuth.getInstance();
        db_auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent intent = new Intent(SplashScreene2.this,MainHomePage.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }
                        }
                    });
    }

    public void GetClick(View view){
        Intent i = new Intent(this, LoginPage.class);
        startActivity(i);
    }
}