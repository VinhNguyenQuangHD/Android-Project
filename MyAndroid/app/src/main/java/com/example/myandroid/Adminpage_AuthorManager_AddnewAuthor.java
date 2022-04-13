package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Adminpage_AuthorManager_AddnewAuthor extends AppCompatActivity {

    EditText authorname,author_description;
    Button button,upload_btn, get_file;
    DatabaseReference author_data;
    TextView author_link;
    ImageView author_img;

    private final int FILE_FROM_DEVICE = 1001;
    Uri link_img;
    String img_firebase_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_author_manager_addnew_author);

        authorname = findViewById(R.id.add_new_author_authortitle_text);
        author_description = findViewById(R.id.add_new_author_author_description_text);
        button = findViewById(R.id.add_new_author_button);
        upload_btn = findViewById(R.id.add_new_author_img_button);
        get_file = findViewById(R.id.add_new_author_file_directory_button);
        author_link = findViewById(R.id.add_new_book_image_file_txt);
        author_img = findViewById(R.id.add_new_author_image_uri);


        author_data = FirebaseDatabase.getInstance().getReference().child("tacgia");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAuthorData();
            }
        });

        get_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgLink();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadAuthorImg();
            }
        });
    }

    private void UploadAuthorImg() {
        String img_path = "Author_img/"+authorname.getText().toString();

        StorageReference upload_img = FirebaseStorage.getInstance().getReference(img_path);
        upload_img.putFile(link_img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                img_firebase_url = ""+uriTask.getResult();

                Toast.makeText(Adminpage_AuthorManager_AddnewAuthor.this, "Tải file hình thành công", Toast.LENGTH_SHORT).show();
                author_link.setText(img_firebase_url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Adminpage_AuthorManager_AddnewAuthor.this, "Không thể tải file hình", Toast.LENGTH_SHORT).show();
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
                    author_link.setText(link_img.getPath().toString());
                }
                break;
        }
    }

    private void insertAuthorData() {
            String new_author_name = authorname.getText().toString();
            String new_author_description = author_description.getText().toString();

            if(new_author_name.isEmpty()){
                authorname.setError("Tên tác giả không được trống");
                authorname.requestFocus();
                return;
            }

            if(new_author_description.isEmpty()){
                author_description.setError("Mô tả tác giả không được trống");
                author_description.requestFocus();
                return;
            }

            Author authors = new Author(new_author_name,new_author_description,author_link.getText().toString());

            author_data.child(new_author_name.replace(".","")).setValue(authors).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(Adminpage_AuthorManager_AddnewAuthor.this,"Đã thêm tác giả thành công",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Adminpage_AuthorManager_AddnewAuthor.this, "Không thể thêm được tác giả, do:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



    }
}