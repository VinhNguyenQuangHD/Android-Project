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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Adminpage_BookManager_edit_book extends AppCompatActivity {

    EditText book_name,book_author,book_date,book_type,book_point,book_watch;
    TextView img_link, file_src_link;
    Button confirm_update_btn, upload_file_btn, img_path,file_path;

    private final int FILE_FROM_DEVICE = 1001;

    Uri img_uri,pdf_uri;
    String img_firebase_url,pdf_firebase_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_book_manager_edit_book);

        book_name = findViewById(R.id.edit_book_booktitle_text);
        book_author = findViewById(R.id.edit_book_author_text);
        book_date = findViewById(R.id.edit_book_date_text);
        book_type = findViewById(R.id.edit_book_types_text);
        book_point = findViewById(R.id.edit_book_price_text);
        book_watch = findViewById(R.id.edit_book_view_text);

        img_link = findViewById(R.id.edit_book_image_file_txt);
        file_src_link = findViewById(R.id.add_new_book_src_file_txt);
        upload_file_btn = findViewById(R.id.adminpage_upload_book_button);
        img_path = findViewById(R.id.edit_book_file_directory_button);
        file_path = findViewById(R.id.add_new_book_src_file_directory_button);

        confirm_update_btn = findViewById(R.id.edit_book_button);

        Bundle getExtra = getIntent().getExtras();
        if(getExtra != null){
            book_name.setText(getExtra.getString("book_name"));
        }

        img_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileLink();
            }
        });

        file_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBookSource();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sach");
        databaseReference.orderByChild("book_name")
                .equalTo(book_name.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal detail = dataSnapshot.getValue(Book_overal.class);
                    book_author.setText(detail.getBook_author());
                    book_date.setText(detail.getBook_date());
                    book_type.setText(detail.getBook_type());
                    book_point.setText(detail.getBook_point());
                    book_watch.setText(detail.getBook_watch());

                    img_link.setText(detail.getBook_img());
                    file_src_link.setText(detail.getBook_src());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirm_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDataOnTheList();

            }
        });

        upload_file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPdfAndImage();
            }
        });

    }

    private void SetDataOnTheList(){
        HashMap detail_change = new HashMap();
        detail_change.put("book_name",book_name.getText().toString());
        detail_change.put("book_author",book_author.getText().toString());
        detail_change.put("book_type",book_type.getText().toString());
        detail_change.put("book_date",book_date.getText().toString());
        detail_change.put("book_point",book_point.getText().toString());
        detail_change.put("book_view",book_watch.getText().toString());
        detail_change.put("book_img",img_link.getText().toString());
        detail_change.put("book_src",file_src_link.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("sach");
        databaseReference.child(book_name.getText().toString())
                .updateChildren(detail_change).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(Adminpage_BookManager_edit_book.this, "Đầu sách đã được cập nhật", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(Adminpage_BookManager_edit_book.this, "Không thể cập nhật đầu sách", Toast.LENGTH_SHORT).show();
                }
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
                    img_uri = data.getData();
                    img_link.setText(img_uri.getPath().toString());

                }
                break;
            case FILE_FROM_DEVICE:
                if(requestCode == FILE_FROM_DEVICE && resultCode==RESULT_OK){
                    pdf_uri = data.getData();
                    file_src_link.setText(pdf_uri.getPath().toString());
                }
                break;
        }
    }

    //Tai anh va file len server
    private void UploadPdfAndImage() {
        String new_img_link = img_link.getText().toString();
        String img_path = "Book_img/"+ new_img_link;
        String pdf_path = "Book_file/"+file_src_link.getText().toString();
        //Tai len firebase storage
        //Tai hinh len firebase storage
        StorageReference upload_img = FirebaseStorage.getInstance().getReference(img_path);
        upload_img.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                img_firebase_url = ""+uriTask.getResult();

                Toast.makeText(Adminpage_BookManager_edit_book.this, "Tải file hình thành công", Toast.LENGTH_SHORT).show();
                img_link.setText(img_firebase_url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Adminpage_BookManager_edit_book.this, "Không thể tải file hình", Toast.LENGTH_SHORT).show();
            }
        });

        //Tai file len firebase storage
        StorageReference upload_file = FirebaseStorage.getInstance().getReference(pdf_path);
        upload_file.putFile(pdf_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                pdf_firebase_url = ""+uriTask.getResult();

                Toast.makeText(Adminpage_BookManager_edit_book.this, "Tải file thành công", Toast.LENGTH_SHORT).show();
                file_src_link.setText(pdf_firebase_url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Adminpage_BookManager_edit_book.this, "Không thể tải file pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }
}