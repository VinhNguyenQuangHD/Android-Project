package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AuthorPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    AuthorPage_author_adapter.RecycleViewListenner listen;
    ArrayList<Author> author_list;
    Bundle getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page);

        recyclerView = findViewById(R.id.authorpage_list_list);

        author_list = new ArrayList<>();

        getEmail = getIntent().getExtras();
        OpenAuthor();

        AuthorPage_author_adapter adapter = new AuthorPage_author_adapter(author_list,listen);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tacgia");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                author_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Author author = dataSnapshot.getValue(Author.class);
                    author_list.add(author);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void OpenAuthor() {
        listen = new AuthorPage_author_adapter.RecycleViewListenner() {
            @Override
            public void SelectAuthor(View v, int position) {
                Intent intent = new Intent(AuthorPage.this,AuthorPageDetail.class);
                intent.putExtra("author_name",author_list.get(position).getAuthor_name());
                intent.putExtra("author_description",author_list.get(position).getAuthor_description());
                intent.putExtra("email",getEmail.getString("email"));
                intent.putExtra("point",getEmail.getString("point"));
                intent.putExtra("username",getEmail.getString("username"));
                intent.putExtra("author_img",author_list.get(position).getAuthor_img());
                startActivity(intent);
            }
        };
    }
}