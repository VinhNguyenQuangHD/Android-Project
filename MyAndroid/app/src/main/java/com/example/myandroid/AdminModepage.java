package com.example.myandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AdminModepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_modepage);

    }

    public void Move_to_book_manager(View view){
        startActivity(new Intent(AdminModepage.this,Adminpage_BookManager.class));
    }

    public void Move_to_author(View view){
        startActivity(new Intent(AdminModepage.this,Adminpage_AuthorManager.class));
    }

    public void Move_to_account_manager(View view){
        Bundle bundle = getIntent().getExtras();
        Intent i = new Intent(AdminModepage.this,Adminpage_AccountManager.class);
        i.putExtra("email",bundle.getString("email"));
        startActivity(i);
    }

    public void Move_to_comment_manager(View view){
        Bundle bundle = getIntent().getExtras();
        Intent i = new Intent(AdminModepage.this,Adminpage_CommentManager.class);
        i.putExtra("email",bundle.getString("email"));
        startActivity(i);
    }

    public void Move_to_report_manager(View view){
        Bundle bundle = getIntent().getExtras();
        Intent i = new Intent(AdminModepage.this,Adminpage_ReportManager.class);
        i.putExtra("email",bundle.getString("email"));
        startActivity(i);
    }
}