package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;

public class Adminpage_BookManager_Addnewbook extends AppCompatActivity {

    EditText bookname,author,pricepoint,view;
    TextView imglink,srclink,datetime;
    Button button, imglink_btn, src_link_btn, upload_btn;
    ImageView imageView;

    Spinner types;

    private final int FILE_FROM_DEVICE = 1001;

    StorageReference storageRe, pdfstorageRef;
    Uri imagelink,pdflink;

    DatabaseReference book_overal_data, book_detail_data, book_chapter_data;

    FirebaseAuth auth;
    String img_firebase_url, pdf_firebase_url, type_chooser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_book_manager_addnewbook);
        //Edit text
        bookname = findViewById(R.id.add_new_book_booktitle_text);
        author = findViewById(R.id.add_new_book_author_text);
        pricepoint = findViewById(R.id.add_new_book_price_text);
        view = findViewById(R.id.add_new_book_view_text);

        //Spinner
        types = findViewById(R.id.add_new_book_types_text);

        //Text view
        imglink = findViewById(R.id.add_new_book_image_file_txt);
        srclink = findViewById(R.id.add_new_book_src_file_txt);
        datetime = findViewById(R.id.add_new_book_date_text);
        SetTime();

        //Button
        imglink_btn = findViewById(R.id.add_new_book_file_directory_button);
        button = findViewById(R.id.add_new_book_button);
        src_link_btn = findViewById(R.id.add_new_book_src_file_directory_button);
        upload_btn = findViewById(R.id.upload_button);

        //ImageView
        imageView = findViewById(R.id.add_new_book_book_image_uri);

        book_overal_data = FirebaseDatabase.getInstance().getReference().child("sach");
        book_chapter_data = FirebaseDatabase.getInstance().getReference().child("chuong");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBookData();
            }
        });

        //Lay the loai cho dau sach
        ArrayAdapter<CharSequence> char_adapter = ArrayAdapter.createFromResource(this,R.array.bookdetail_types,R.layout.spinner_drop_down_item);
        char_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setAdapter(char_adapter);
        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String points = parent.getItemAtPosition(position).toString();
                type_chooser = points;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Lay file pdf cua sach
        src_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBookSource();
            }
        });

        //Lay file hinh cua sach
        imglink_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileLink();
            }
        });

        //Dua file hinh va sach len server
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPdfAndImage();
            }
        });

    }

    private void SetTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        datetime.setText(String.valueOf(day+"/"+month+"/"+year));
    }

    //Tai anh va file len server
    private void UploadPdfAndImage() {
        String img_path = "Book_img/"+imglink.getText().toString();
        String pdf_path = "Book_file/"+srclink.getText().toString();
        //Tai len firebase storage
        //Tai hinh len firebase storage
        StorageReference upload_img = FirebaseStorage.getInstance().getReference(img_path);
        upload_img.putFile(imagelink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                img_firebase_url = ""+uriTask.getResult();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Adminpage_BookManager_Addnewbook.this, "Không thể tải file hình", Toast.LENGTH_SHORT).show();
            }
        });

        //Tai file len firebase storage
        StorageReference upload_file = FirebaseStorage.getInstance().getReference(pdf_path);
        upload_file.putFile(pdflink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                pdf_firebase_url = ""+uriTask.getResult();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Adminpage_BookManager_Addnewbook.this, "Không thể tải file pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Lay duong dan file tu thiet bi
    private void insertBookSource() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,FILE_FROM_DEVICE);
    }

    //Lay duong dan hinh tu thiet bi
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
                    imageView.setImageURI(imagelink);
                    imglink.setText(imagelink.getPath().toString());

                }
                break;
            case FILE_FROM_DEVICE:
                if(requestCode == FILE_FROM_DEVICE && resultCode==RESULT_OK){
                    pdflink = data.getData();
                    srclink.setText(data.getData().getPath().toString());
                }
                break;
        }
    }


    //Them dau sach vao database
    private void insertBookData() {
        String new_book_name = bookname.getText().toString();
        String new_book_author = author.getText().toString();
        String new_book_date = datetime.getText().toString();
        String new_book_price = pricepoint.getText().toString();
        String new_book_view = view.getText().toString();
        final String[] new_book_imglink = {""};
        final String[] new_book_srcfile = {""};
        String new_book_types = type_chooser;

        if(new_book_name.isEmpty()){
            bookname.setError("Tên sách không được trống");
            bookname.requestFocus();
            return;
        }

        if(new_book_author.isEmpty()){
            author.setError("Tên tác giả không được trống");
            author.requestFocus();
            return;
        }

        if(new_book_date.isEmpty()){
            datetime.setError("Thời gian không được trống");
            datetime.requestFocus();
            return;
        }

        if(new_book_view.isEmpty()){
            view.setError("Lượt đọc không được trống");
            view.requestFocus();
            return;
        }

        DatabaseReference book_references = FirebaseDatabase.getInstance().getReference("sach");
        Book_overal add_book_overal = new Book_overal(new_book_author,new_book_name,"0",
                new_book_types,new_book_price,new_book_date,img_firebase_url, pdf_firebase_url,new_book_view);

        //Su kien them dau sach
        book_references.child(new_book_name).setValue(add_book_overal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Adminpage_BookManager_Addnewbook.this, "Đầu sách đã được thêm vào thành công", Toast.LENGTH_SHORT).show();
                author.setText("");
                view.setText("");
                pricepoint.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Adminpage_BookManager_Addnewbook.this, "Không thể thêm được đầu sách, do:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}