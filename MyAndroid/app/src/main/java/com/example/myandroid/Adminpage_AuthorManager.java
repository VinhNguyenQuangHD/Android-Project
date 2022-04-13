package com.example.myandroid;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Adminpage_AuthorManager extends AppCompatActivity {

    Button button;
    RecyclerView author_list;
    ImageButton search_btn;
    EditText author_text;

    ArrayList<Author> author_list_array;

    private Adminpage_AuthorManager_Author_list_adapter.RecyclerviewClick listenner;
    Adminpage_AuthorManager_Author_list_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_author_manager);
        button = findViewById(R.id.add_new_author_btn);
        author_list = findViewById(R.id.list_of_curent_author);
        search_btn = findViewById(R.id.search_author_btn);
        author_text = findViewById(R.id.get_author_find_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Adminpage_AuthorManager.this,Adminpage_AuthorManager_AddnewAuthor.class));
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchEven();
            }
        });

        author_list_array = new ArrayList<>();
        Update_delete_event(author_list_array);
        adapter = new Adminpage_AuthorManager_Author_list_adapter(author_list_array,listenner);

        DatabaseReference author_reference = FirebaseDatabase.getInstance().getReference("tacgia");
        author_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                author_list_array.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Author author = dataSnapshot.getValue(Author.class);
                    author_list_array.add(author);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        author_list.setLayoutManager(new LinearLayoutManager(this));
        author_list.setItemAnimator(new DefaultItemAnimator());
        author_list.setAdapter(adapter);
    }

    private void Update_delete_event(ArrayList<Author> list) {
        listenner = new Adminpage_AuthorManager_Author_list_adapter.RecyclerviewClick() {
            @Override
            public void onClickDelete(View v, int position) {
                String key = list.get(position).getAuthor_name().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("tacgia").orderByChild("author_name").equalTo(key);

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
                Intent i = new Intent(Adminpage_AuthorManager.this,Adminpage_AuthorManager_edit_author.class);
                i.putExtra("author_img_src",list.get(position).getAuthor_img());
                i.putExtra("author_name",list.get(position).getAuthor_name());
                i.putExtra("author_description",list.get(position).getAuthor_description());
                startActivity(i);
            }
        };
    }

    private void SearchEven() {
        author_list.setLayoutManager(new LinearLayoutManager(this));
        author_list_array = new ArrayList<>();


        DatabaseReference search_ref = FirebaseDatabase.getInstance().getReference("tacgia");
        search_ref.orderByChild("book_name").startAt(author_text.getText().toString()).endAt(author_text.getText().toString() + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                author_list_array.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Author overal = dataSnapshot.getValue(Author.class);
                    author_list_array.add(overal);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Update_delete_event(author_list_array);
        adapter = new Adminpage_AuthorManager_Author_list_adapter(author_list_array,listenner);
        author_list.setItemAnimator(new DefaultItemAnimator());
        author_list.setAdapter(adapter);


    }

}