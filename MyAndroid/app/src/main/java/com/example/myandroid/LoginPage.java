package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    EditText textView,textView2;
    Button button;
    ImageButton google_login_btn;
    CheckBox remember_me;
    TextView forgot_passwords, create_admin_account;

    private FirebaseAuth db_auth;
    private GoogleSignInClient google_client;
    private CallbackManager facebook_call_back;

    private static final int RC_SIGN_IN = 9001;
    String get_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        //ImageButton
        google_login_btn = (ImageButton) findViewById(R.id.google_login_btn);

        //EditText
        textView = (EditText) findViewById(R.id.edittext_username);
        textView2 = (EditText) findViewById(R.id.edittext_password);

        //CheckBox
        remember_me = findViewById(R.id.remember_pass);
        remember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    RememberLogin(textView.getText().toString(),textView2.getText().toString());
                }
            }
        });

        //TextView
        create_admin_account = findViewById(R.id.create_account_admin_text);
        create_admin_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,AdminSignUpPage.class));
            }
        });

        textView.setText("");
        textView2.setText("");

        //Mo trang quen mat khau neu nhu ban khong nho mat khau truoc do
        forgot_passwords = (TextView) findViewById(R.id.forgot_password);
        forgot_passwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(LoginPage.this,ForgotPasswordsPage.class);
                    i.putExtra("emails",textView.getText().toString());
                    startActivity(i);
            }
        });

        String username_acc = textView.getText().toString();

        button = findViewById(R.id.login_button);
        button.setOnClickListener(this);

        //Google_code
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        google_client = GoogleSignIn.getClient(this,gso);

        db_auth = FirebaseAuth.getInstance();

        //Dang nhap qua tai khoan google
        google_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleLoginEven();
            }
        });

    }

    //Nho thong tin dang nhap
    private void RememberLogin(String toString, String toString1) {
        SharedPreferences.Editor edit = getSharedPreferences("MyFile",MODE_PRIVATE).edit();
        edit.putString("username",toString);
        edit.putString("password",toString1);
        edit.apply();
        Toast.makeText(LoginPage.this, "Tài khoản đã được nhớ", Toast.LENGTH_SHORT).show();
    }

    //Phuong thuc dang nhap google
    private void GoogleLoginEven() {
        Intent signInIntent = google_client.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                Toast.makeText(LoginPage.this, "Đăng nhập google thành công", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginPage.this, "Đăng nhập google không thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        db_auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //lay thong tin dang nhap
                            FirebaseUser google_user = db_auth.getCurrentUser();

                            //Luu vao tai khoan
                            DatabaseReference google_acc_ref = FirebaseDatabase.getInstance().getReference("taikhoan");
                            Account google_account = new Account(google_user.getDisplayName().toString(),google_user.getEmail().toString(),"Mật khẩu của Google");
                            google_acc_ref.child(google_user.getEmail().replace(".","").toString()).setValue(google_account);

                            //Luu vao thong tin tai khoan
                            DatabaseReference google_acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                            Account_infor google_account_infor = new Account_infor
                                    (google_user.getDisplayName().toString(),"0",google_user.getEmail().toString(),google_user.getPhotoUrl().toString(),"0","Google user");
                            google_acc_infor_ref.child(google_user.getEmail().replace(".","").toString()).setValue(google_account_infor);

                            Toast.makeText(LoginPage.this, "Đăng nhập google thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginPage.this,MainHomePage.class);
                            intent.putExtra("email",google_user.getEmail().toString());
                            intent.putExtra("type","0");
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Đăng nhập google thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void Change_to_SignUp(View view){
        Intent intent = new Intent(this,SignInPage.class);
        startActivity(intent);
    }

    //Dang nhap vao trang chu
    public void Change_to_mainpage(){

        String get_email = textView.getText().toString().trim();
        String get_passwords = textView2.getText().toString().trim();


        if(get_email.isEmpty() ){
            textView.setError("Email không được trống");
            textView.requestFocus();
            return;}
        if(get_passwords.isEmpty()){
            textView2.setError("Mật khẩu không được trống");
            textView2.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(get_email).matches()){
            textView.setError("Email phải hợp lệ");
            textView.requestFocus();
            return;
        }
        //Kiem tra tai khoan
        get_type = "";
        DatabaseReference acc_check_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        acc_check_ref.orderByChild("email").equalTo(get_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    get_type = infor.getAcc_type();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LoginState();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                Change_to_mainpage();
                break;
        }
    }
    public void LoginState(){
        db_auth.signInWithEmailAndPassword(textView.getText().toString(),textView2.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            if(remember_me.isChecked()){
                                Paper.book().write(Account_remember.remember_username,textView.getText().toString());
                                Paper.book().write(Account_remember.remember_password,textView2.getText().toString());
                            }

                            if(get_type.equals("disable")){
                                BanNotice(Gravity.CENTER);
                            }
                            else if(get_type.equals("admin")){
                                FirebaseUser user = db_auth.getCurrentUser();
                                if(user.isEmailVerified()){
                                    Intent intent = new Intent(LoginPage.this,MainHomePage.class);
                                    intent.putExtra("email",textView.getText().toString());
                                    intent.putExtra("type",get_type);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginPage.this, "Tài khoản chưa được xác thực", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Intent intent = new Intent(LoginPage.this,MainHomePage.class);
                                intent.putExtra("email",textView.getText().toString());
                                intent.putExtra("type",get_type);
                                startActivity(intent);
                            }

                        }else{
                            Toast.makeText(LoginPage.this, "Đăng nhập thất bại,vui lòng thử lại",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void BanNotice(int gravity) {
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_loginpage_banned_notification);

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
        //Them chuong sach moi

        Button cancel_btn = custom_dialog.findViewById(R.id.loginpage_cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });
        custom_dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = db_auth.getCurrentUser();
        //updateUI(currentUser);
    }
}