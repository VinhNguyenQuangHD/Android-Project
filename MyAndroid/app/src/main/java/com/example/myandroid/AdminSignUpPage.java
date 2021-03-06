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
                    Toast.makeText(AdminSignUpPage.this, "File h??nh kh??ng t??m th???y", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(AdminSignUpPage.this, "T???i file h??nh th??nh c??ng", Toast.LENGTH_SHORT).show();
                img_file.setText(img_firebase_url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminSignUpPage.this, "Kh??ng th??? t???i file h??nh", Toast.LENGTH_SHORT).show();
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
            admin_email.setError("Email kh??ng ???????c tr???ng");
            admin_email.requestFocus();
            return;
        }else if(get_username.isEmpty() ){
            admin_username.setError("T??n ????ng nh???p kh??ng ???????c tr???ng");
            admin_username.requestFocus();
            return;
        } else if(get_passwords.isEmpty()){
            admin_pass.setError("M???t kh???u kh??ng ???????c tr???ng");
            admin_pass.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(get_email).matches()){
            admin_email.setError("Email ph???i h???p l???");
            admin_email.requestFocus();
            return;
        }
        else if(!get_re_write_password.equals(get_passwords)){
            admin_confirm_pass.setError("M???t kh???u nh???p l???i kh??ng kh???p");
            admin_confirm_pass.requestFocus();
            return;
        }else if(get_passwords.length() < 6){
            admin_pass.setError("????? d??i m???t kh???u kh??ng d?????i 6 k?? t???");
            admin_pass.requestFocus();
            return;
        }else if(get_username.length() >= 21){
            admin_username.setError("T??n ????ng nh???p qu?? d??i");
            admin_username.requestFocus();
            return;
        }
        else if(img_file.getText().equals(old_link)){
            Toast.makeText(AdminSignUpPage.this, "File h??nh kh??ng t??m th???y", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(AdminSignUpPage.this, "???? g???i qua email th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminSignUpPage.this, "???? c?? l???i x???y ra:"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(AdminSignUpPage.this, "T??i kho???n admin ???? ???????c ????ng k?? th??nh c??ng, vui l??ng x??c th???c ????? ???????c ????ng nh???p",Toast.LENGTH_LONG).show();

                                        }else{
                                            Toast.makeText(AdminSignUpPage.this, "????ng k?? th???t b???i,vui l??ng th??? l???i",Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                });
                            }else
                            {
                                Toast.makeText(AdminSignUpPage.this, "Kh??ng th??? ????ng k??",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }
}