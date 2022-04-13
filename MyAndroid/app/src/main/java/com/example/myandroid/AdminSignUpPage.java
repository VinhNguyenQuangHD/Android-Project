package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminSignUpPage extends AppCompatActivity {

    EditText admin_email,admin_username,admin_pass,admin_confirm_pass;
    Button sing_in_admin_btn, upload_to_sv_btn;
    TextView back_to_login, img_file;
    ProgressBar progressBar;
    String old_link,img_firebase_url;
    Uri link_img;

    private FirebaseAuth db_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up_page);

        //Edit Text
        admin_email = findViewById(R.id.edittext_signup_admin_email);
        admin_username = findViewById(R.id.edittext_signup_admin_username);
        admin_pass = findViewById(R.id.edittext_signup_admin_password);
        admin_confirm_pass = findViewById(R.id.edittext_signup_admin_rewrite_password);

        //Button
        sing_in_admin_btn = findViewById(R.id.admin_signin_button);
        sing_in_admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSignUpEven();
            }
        });

        db_auth = FirebaseAuth.getInstance();
        old_link = "#hinhadmin";

        //TextView
        back_to_login = findViewById(R.id.admin_back_to_login);
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSignUpPage.this,LoginPage.class));
            }
        });

        //ProgressBar
        progressBar = findViewById(R.id.admin_progress_stage);

        //TextView
        img_file = findViewById(R.id.signup_get_img_text);
        img_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgLink();
            }
        });

        //Button
        upload_to_sv_btn = findViewById(R.id.btn_upload_to_server_btn);
        upload_to_sv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(img_file.getText().equals(old_link)){
                    Toast.makeText(AdminSignUpPage.this, "File hình không tìm thấy", Toast.LENGTH_SHORT).show();
                }else{
                    UploadProfileImg();
                }
            }
        });
    }

    private void UploadProfileImg() {
        String img_path = "Profile_img/"+img_file.getText().toString();

        StorageReference upload_img = FirebaseStorage.getInstance().getReference(img_path);
        upload_img.putFile(link_img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                img_firebase_url = ""+uriTask.getResult();

                Toast.makeText(AdminSignUpPage.this, "Tải file hình thành công", Toast.LENGTH_SHORT).show();
                img_file.setText(img_firebase_url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminSignUpPage.this, "Không thể tải file hình", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImgLink() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    link_img = data.getData();
                    img_file.setText(link_img.getPath().toString());
                }
                break;
        }
    }

    private void AdminSignUpEven() {
        String get_email = admin_email.getText().toString().trim();
        String get_username = admin_username.getText().toString().trim();
        String get_passwords = admin_pass.getText().toString().trim();
        String get_re_write_password = admin_confirm_pass.getText().toString().trim();
        if(get_email.isEmpty() ){
            admin_email.setError("Email không được trống");
            admin_email.requestFocus();
            return;
        }else if(get_username.isEmpty() ){
            admin_username.setError("Tên đăng nhập không được trống");
            admin_username.requestFocus();
            return;
        } else if(get_passwords.isEmpty()){
            admin_pass.setError("Mật khẩu không được trống");
            admin_pass.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(get_email).matches()){
            admin_email.setError("Email phải hợp lệ");
            admin_email.requestFocus();
            return;
        }
        else if(!get_re_write_password.equals(get_passwords)){
            admin_confirm_pass.setError("Mật khẩu nhập lại không khớp");
            admin_confirm_pass.requestFocus();
            return;
        }else if(get_passwords.length() < 6){
            admin_pass.setError("Độ dài mật khẩu không dưới 6 ký tự");
            admin_pass.requestFocus();
            return;
        }else if(get_username.length() >= 21){
            admin_username.setError("Tên đăng nhập quá dài");
            admin_username.requestFocus();
            return;
        }
        else if(img_file.getText().equals(old_link)){
            Toast.makeText(AdminSignUpPage.this, "File hình không tìm thấy", Toast.LENGTH_SHORT).show();
        }
        else{
            db_auth.createUserWithEmailAndPassword(admin_email.getText().toString(),admin_pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                //Lay thong tin nguoi dung
                                FirebaseUser user = db_auth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdminSignUpPage.this, "Đã gửi qua email thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminSignUpPage.this, "Đã có lỗi xảy ra:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //Tao thong tin dang nhap
                                Account account = new Account(get_username,get_email,get_passwords);
                                FirebaseDatabase.getInstance().getReference("taikhoan").
                                        child(get_email.replace(".",""))
                                        .setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("thongtintaikhoan");
                                            Account_infor infor = new Account_infor(get_username,"0",get_email,img_file.getText().toString(),"admin","khong co mo ta");
                                            databaseReference.child(get_email.replace(".","")).setValue(infor);

                                            startActivity(new Intent(AdminSignUpPage.this,LoginPage.class));

                                            progressBar.setVisibility(View.VISIBLE);
                                            Toast.makeText(AdminSignUpPage.this, "Tài khoản admin đã được đăng ký thành công, vui lòng xác thực để được đăng nhập",Toast.LENGTH_LONG).show();

                                        }else{
                                            Toast.makeText(AdminSignUpPage.this, "Đăng ký thất bại,vui lòng thử lại",Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                });
                            }else
                            {
                                Toast.makeText(AdminSignUpPage.this, "Không thể đăng ký",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }
}