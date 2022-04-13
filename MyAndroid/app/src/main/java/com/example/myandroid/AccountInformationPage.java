package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountInformationPage extends AppCompatActivity {

    TextView username,email,type,description_text;
    Button custom_profile;
    TextView img_link;

    String emailss,img_firebase_url;

    RecyclerView all_account_list,friend_list, book_record_list;

    ImageView user_img_display;
    ImageButton delete_history_btn;

    AccountInformationPage_all_account_adapter.RecyclerviewClick listenner;
    AccountInformationPage_friend_list_adapter.RecyclerviewClick click;

    ArrayList<Account_infor> acc_list;
    ArrayList<Friend_req> friend_list_array;
    ArrayList<Book_history> book_history_list;

    Uri imagelink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information_page);

        username = findViewById(R.id.accountpage_text_username);
        email = findViewById(R.id.accountpage_email_username);
        type = findViewById(R.id.accountpage_type_username);
        custom_profile = findViewById(R.id.edit_profile);
        description_text = findViewById(R.id.accountpage_description_username);

        book_record_list = findViewById(R.id.book_record_list);

        all_account_list = findViewById(R.id.username_recommend_list);
        friend_list = findViewById(R.id.friend_list);

        user_img_display = findViewById(R.id.accountpage_img_username);
        delete_history_btn = findViewById(R.id.clear_history_btn);

        //Hien thi email dang nhap
        Bundle getEmail_text = getIntent().getExtras();
        emailss = getEmail_text.getString("email");
        email.setText(emailss);


        //Load thong tin nguoi dung
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        databaseReference.orderByChild("email").equalTo(email.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    Glide.with(user_img_display.getContext().getApplicationContext()).load(infor.getImgsrc())
                            .apply(RequestOptions.circleCropTransform()).into(user_img_display);
                    username.setText(infor.getUsername());
                    if(infor.getAcc_type().equals("0")){
                        type.setText("Người dùng thông thường");
                    }else if(infor.getAcc_type().equals("VIP")){
                        type.setText("Thành viên VIP");
                    }else{
                        type.setText("Quản trị viên");
                    }
                    description_text.setText(infor.getDescription());
                    Glide.with(user_img_display.getContext()).load(infor.getImgsrc())
                            .apply(RequestOptions.circleCropTransform()).into(user_img_display);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Hien thi thay doi thong tin
        custom_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogSetUp(Gravity.CENTER);
            }
        });


        //Hien thi toan bo tai khoan
        LoadAllAccount();
        //Hien thi toan bo danh sach ban be
        LoadAllFriend();
        //Hien thi toan bo lich su doc sach
        LoadAllBookRecord();

        delete_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = email.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("lichsu").orderByChild("email").equalTo(key);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(AccountInformationPage.this, "Đã xóa lịch sử đọc sách của bạn", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadAllBookRecord() {
        book_history_list = new ArrayList<>();
        book_record_list.setLayoutManager(new LinearLayoutManager(this));

        AccountInformationPage_book_record_adapter adapter = new AccountInformationPage_book_record_adapter(book_history_list);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("lichsu");
        databaseReference.orderByChild("email").equalTo(email.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                book_history_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_history req = dataSnapshot.getValue(Book_history.class);
                    book_history_list.add(req);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        book_record_list.setItemAnimator(new DefaultItemAnimator());
        book_record_list.setAdapter(adapter);
    }

    //Lay danh sach ban be
    private void LoadAllFriend() {

        friend_list_array = new ArrayList<>();
        friend_list.setLayoutManager(new LinearLayoutManager(this));
        DeleteFriendEvent();

        AccountInformationPage_friend_list_adapter adapter = new AccountInformationPage_friend_list_adapter(friend_list_array,click);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("banbe");
        databaseReference.orderByChild("email_send").equalTo(email.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend_list_array.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend_req req = dataSnapshot.getValue(Friend_req.class);
                    friend_list_array.add(req);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friend_list.setItemAnimator(new DefaultItemAnimator());
        friend_list.setAdapter(adapter);
    }

    //Xoa ban be khoi danh sach
    private void DeleteFriendEvent() {
        click = new AccountInformationPage_friend_list_adapter.RecyclerviewClick() {
            @Override
            public void DeleteRequest(View v, int position) {
                String key = friend_list_array.get(position).getEmail_send().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("banbe").orderByChild("email_send").equalTo(key);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(AccountInformationPage.this, "Đã xóa bạn bè khỏi danh sách", Toast.LENGTH_SHORT).show();
            }
        };
    }

    //Hien thi dialog chinh sua thong tin tai khoan
    private void CustomDialogSetUp(int gravity) {
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.account_informationpage_edit_dialog);

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
        EditText username_name = custom_dialog.findViewById(R.id.editprofile_username_text);
        EditText username_description = custom_dialog.findViewById(R.id.editprofile_descriptions_text);

        img_link = custom_dialog.findViewById(R.id.editprofile_img_link);

        Button upload_img_btn = custom_dialog.findViewById(R.id.upload_profile_img_confirm);
        Button get_img_link = custom_dialog.findViewById(R.id.editprofile_img_link_btn);
        Button got_cancel = custom_dialog.findViewById(R.id.edit_dialog_cancel);
        Button got_edit = custom_dialog.findViewById(R.id.edit_dialog_confirm);


        //Lay anh tu thu vien trong dt
        get_img_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileLink();
            }
        });
        //Tai anh len db
        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImgOnDB(username_name.getText().toString());

            }
        });
        DatabaseReference account_infor_Reference = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");

        account_infor_Reference.orderByChild("username").equalTo(username.getText().toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor acc = dataSnapshot.getValue(Account_infor.class);
                            username_name.setText(acc.getUsername());
                            username_description.setText(acc.getDescription());
                            img_link.setText(acc.getImgsrc());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        got_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

        got_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap detail_change = new HashMap();
                detail_change.put("description",username_description.getText().toString());
                detail_change.put("username",username_name.getText().toString());
                detail_change.put("imgsrc",img_link.getText().toString());

                HashMap username_change = new HashMap();
                username_change.put("username", username_name.getText().toString());

                String get_email = email.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                databaseReference.child(get_email.replace(".","")).updateChildren(detail_change).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AccountInformationPage.this, "Thông tin đã được thay đổi !!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AccountInformationPage.this, "Thông tin không thể thay đổi !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("taikhoan");
                databaseReferences.child(get_email.replace(".","")).updateChildren(username_change);

            }
        });

        custom_dialog.show();
    }

    //Tai hinh len db
    private void UploadImgOnDB(String username_name) {
            String img_path = "Profile_img/"+img_link.getText().toString();
            //Tai len firebase storage
            //Tai hinh len firebase storage
            StorageReference upload_img = FirebaseStorage.getInstance().getReference(img_path);
            upload_img.putFile(imagelink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    img_firebase_url = ""+uriTask.getResult();

                    Toast.makeText(AccountInformationPage.this, "Tải file hình thành công", Toast.LENGTH_SHORT).show();
                    img_link.setText(img_firebase_url);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AccountInformationPage.this, "Không thể tải file hình", Toast.LENGTH_SHORT).show();
                }
            });
    }

    //Tai len toan bo tai khoan
    private void LoadAllAccount(){
        acc_list = new ArrayList<>();
        SendingEvent();

        AccountInformationPage_all_account_adapter all_account_adapter = new AccountInformationPage_all_account_adapter(acc_list,listenner);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acc_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    if(!infor.getEmail().equals(email.getText().toString())){
                        acc_list.add(infor);
                    }
                    all_account_adapter.notifyDataSetChanged();

                    if(acc_list.size() == 6){
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        all_account_list.setLayoutManager(new LinearLayoutManager(this));
        all_account_list.setItemAnimator(new DefaultItemAnimator());
        all_account_list.setAdapter(all_account_adapter);
    }

    //Gui yeu cau ket ban
    private void SendingEvent() {
        listenner = new AccountInformationPage_all_account_adapter.RecyclerviewClick() {
            @Override
            public void SendingRequest(View v, int position) {
                if(acc_list.get(position).getAcc_type().equals("disable")){
                    Toast.makeText(AccountInformationPage.this, "Bạn không thể kết bạn với tài khoản bị cấm", Toast.LENGTH_SHORT).show();
                }else if(acc_list.get(position).getAcc_type().equals("admin")){
                    Toast.makeText(AccountInformationPage.this, "Bạn không thể kết bạn với tài khoản quản trị viên", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference friend = FirebaseDatabase.getInstance().getReference("ketban");
                    Notification notification = new Notification(email.getText().toString(),acc_list.get(position).getEmail(),username.getText().toString(),"1");
                    String emails = email.getText().toString();
                    friend.child(emails.replace(".","")).setValue(notification);

                    Toast.makeText(AccountInformationPage.this, "Đã gửi yêu cầu kết bạn", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    //Lay duong dan file hinh anh
    void getFileLink(){
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
                    imagelink = data.getData();
                    img_link.setText(imagelink.getPath().toString());

                }
                break;
        }
    }

}