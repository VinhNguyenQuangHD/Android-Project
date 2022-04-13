package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OptionPage extends AppCompatActivity {

    ImageButton admin_mode_btn, account_btn, notification_btn, author_btn, vip_btn, logout_btn;

    String email,types,username,point;

    int current_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);

        Bundle get_email_string = getIntent().getExtras();
        email = get_email_string.getString("emails");
        types = get_email_string.getString("types");
        username = get_email_string.getString("username");
        point = get_email_string.getString("point");

        admin_mode_btn = findViewById(R.id.admin_modes_btn);
        admin_mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(types.equals("admin")){
                    Intent i = new Intent(OptionPage.this,AdminModepage.class);
                    i.putExtra("email",email);
                    startActivity(i);
                }else
                {
                    Toast.makeText(OptionPage.this, "Chỉ những tài khoản quản trị viên mới được truy cập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        account_btn = findViewById(R.id.account_option_btn);
        account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionPage.this,AccountInformationPage.class);
                i.putExtra("email",email);
                startActivity(i);
            }
        });

        notification_btn = findViewById(R.id.personal_option_btn);
        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionPage.this,NotificationPage.class);
                i.putExtra("email",email);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        author_btn = findViewById(R.id.author_list_btn);
        author_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionPage.this,AuthorPage.class);
                i.putExtra("email",email);
                i.putExtra("point",point);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        vip_btn = findViewById(R.id.become_membership_btn);
        vip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenVipLayout(Gravity.CENTER);
            }
        });

        logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionPage.this,LoginPage.class));
            }
        });

    }

    private void OpenVipLayout(int gravity) {
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_option_page_vip_member);

        Window window = custom_dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win_attr = window.getAttributes();
        win_attr.gravity = gravity;
        window.setAttributes(win_attr);

        if(Gravity.BOTTOM == gravity){
            custom_dialog.setCancelable(true);
        }else{
            custom_dialog.setCancelable(false);
        }

        int Vip_request = 5000;


        Button getting_vip = custom_dialog.findViewById(R.id.become_vip_confirm);
        getting_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lay thong tin ve so diem nguoi dung
                DatabaseReference user_point_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                user_point_ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                            current_point = Integer.parseInt(infor.getPoint());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //So diem
                if(types.equals("VIP")){
                    Toast.makeText(OptionPage.this, "Bạn đã là thành viên vip", Toast.LENGTH_SHORT).show();
                    custom_dialog.dismiss();
                }
                else if(Vip_request > current_point){
                    Toast.makeText(OptionPage.this, "Điểm yêu cầu không đủ", Toast.LENGTH_SHORT).show();
                    custom_dialog.dismiss();
                }
                else{
                    int remaint_point = current_point - Vip_request;
                    String text_vip = "VIP";
                    HashMap change_profile = new HashMap();
                    change_profile.put("acc_type",text_vip);
                    change_profile.put("point",String.valueOf(remaint_point));

                    DatabaseReference profile_change_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                    profile_change_ref.child(email.replace(".","")).updateChildren(change_profile).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(OptionPage.this, "Chúc mừng, bây giờ bạn đã trở thành thành viên VIP", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(OptionPage.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    custom_dialog.dismiss();
                }
            }
        });

        custom_dialog.show();
    }
}