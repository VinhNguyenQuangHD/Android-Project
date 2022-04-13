package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopicDiscursion extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    RecyclerView recyclerView;

    private ArrayList<Book_overal> list;

    private Homepage_recommend_adapter adapter;
    private Homepage_recommend_adapter.RecyclerviewClick listenner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_discursion);

        textView = findViewById(R.id.topic_name_represent);
        imageView = findViewById(R.id.topic_image_represent);
        recyclerView = findViewById(R.id.topic_name_list);


        Bundle bundle = getIntent().getExtras();
        int count = Integer.parseInt(bundle.getString("image"));

        if(count == 1){imageView.setImageResource(R.drawable.adventure_topic);}
        if(count == 2){imageView.setImageResource(R.drawable.travel_topic);}
        if(count == 3){imageView.setImageResource(R.drawable.science_topic);}
        if(count == 4){imageView.setImageResource(R.drawable.fiction_topic);}
        if(count == 5){imageView.setImageResource(R.drawable.life_topic);}
        if(count == 6){imageView.setImageResource(R.drawable.romantic_topic);}
        if(count == 7){imageView.setImageResource(R.drawable.horror_topic);}
        if(count == 8){imageView.setImageResource(R.drawable.detective_topic);}

        textView.setText(bundle.getString("title"));


        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list = new ArrayList<>();

        String search_request =  textView.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sach");
        ref.orderByChild("book_type").equalTo(search_request).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    list.add(overal);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setOnClickListenner();

        adapter = new Homepage_recommend_adapter(listenner,list);
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListenner() {
        listenner = new Homepage_recommend_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {
                Intent i = new Intent(TopicDiscursion.this,BookDetailPage.class);
                i.putExtra("book_name",list.get(position).getBook_name());
                i.putExtra("book_author",list.get(position).getBook_author());
                i.putExtra("book_watch",list.get(position).getBook_watch());
                startActivity(i);
            }
        };
    }
}