package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adminpage_BookManager extends AppCompatActivity {

    Button new_book_btn,new_chapter_btn;
    private ArrayList<Book_overal> list;
    private RecyclerView recycleview;
    private Adminpage_option_update_delete_adapter adapter;
    private Adminpage_option_update_delete_adapter.RecyclerviewClick click;
    
    String search_text;
    ImageButton search_btn;
    ArrayList<Book_overal> search_list;
    EditText search_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_adminpage_book_manager);

        new_book_btn = findViewById(R.id.add_new_book_addbook_btn);
        recycleview = findViewById(R.id.list_of_curent_book);

        search_btn = findViewById(R.id.adminpage_book_search);
        search_txt = findViewById(R.id.adminpage_book_search_text);


        new_chapter_btn = findViewById(R.id.add_new_book_addchapter_btn);
        new_chapter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewBookChapter(Gravity.CENTER);
            }
        });


        //Them dau sach moi
        new_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Adminpage_BookManager.this,Adminpage_BookManager_Addnewbook.class));
            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        //Tai len danh sach toan bo dau sach
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sach");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    list.add(overal);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Tim kiem dau sach
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchEven();
            }
        });
        SetOnDeleteClick();
        adapter = new Adminpage_option_update_delete_adapter(click, list);
        recycleview.setAdapter(adapter);
    }

    //Tim kiem dau sach
    private void SearchEven() {
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();


        DatabaseReference search_ref = FirebaseDatabase.getInstance().getReference("sach");
        search_ref.orderByChild("book_name").startAt(search_txt.getText().toString()).endAt(search_txt.getText().toString() + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    list.add(overal);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        SetOnDeleteClick();
        adapter = new Adminpage_option_update_delete_adapter(click, list);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setAdapter(adapter);


    }

    //Them chuong sach
    private void AddNewBookChapter(int center) {
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_adminpage_bookmanager_add_new_chapter_book);

        Window window = custom_dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win_attr = window.getAttributes();
        win_attr.gravity = center;
        window.setAttributes(win_attr);

        if(Gravity.BOTTOM == center){
            custom_dialog.setCancelable(true);
        }else{
            custom_dialog.setCancelable(false);
        }
        //Them chuong sach moi
        AddNewChapter(custom_dialog);
        custom_dialog.show();
    }

    //Them chuong sach moi_su kien
    private void AddNewChapter(Dialog custom_dialog) {
        Button cancel_btn = custom_dialog.findViewById(R.id.cancel_chapter_btn);
        Button add_new_chapter = custom_dialog.findViewById(R.id.add_book_chapter_btn);

        EditText edit_book_name = custom_dialog.findViewById(R.id.add_new_chapter_book_bookname_text);
        EditText edit_book_chapter = custom_dialog.findViewById(R.id.add_new_chapter_book_part_text);
        EditText edit_book_page = custom_dialog.findViewById(R.id.add_new_chapter_page_text);
        
        add_new_chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Book_chapter chapter = new Book_chapter(edit_book_chapter.getText().toString(),edit_book_page.getText().toString(),edit_book_name.getText().toString());
                    DatabaseReference book_chapter_ref = FirebaseDatabase.getInstance().getReference("chuong");
                    book_chapter_ref.push().setValue(chapter);
                    Toast.makeText(Adminpage_BookManager.this, "Đã thêm thành công chương sách", Toast.LENGTH_SHORT).show();
                    edit_book_chapter.setText("");
                    edit_book_page.setText("");
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

    }

    //Xoa dau sach
    private void SetOnDeleteClick() {
        click = new Adminpage_option_update_delete_adapter.RecyclerviewClick() {
            @Override
            public void onClickDelete(View v, int position) {
                String key = list.get(position).getBook_name().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("sach").orderByChild("book_name").equalTo(key);


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

            }

            @Override
            public void onClickUpdate(View v, int position) {
                Intent intent = new Intent(Adminpage_BookManager.this,Adminpage_BookManager_edit_book.class);
                intent.putExtra("book_name",list.get(position).getBook_name());
                startActivity(intent);
            }
        };
    }


}