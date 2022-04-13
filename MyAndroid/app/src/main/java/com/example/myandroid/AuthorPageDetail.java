package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorPageDetail extends AppCompatActivity {
    TextView author_name,author_des;

    RecyclerView author_list;
    ArrayList<Book_overal> list;
    Homepage_recommend_adapter.RecyclerviewClick listenner;

    CircleImageView author_img;

    String email,username,point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page_detail);

        author_name = findViewById(R.id.author_name_represent);
        author_des = findViewById(R.id.author_description_text);

        author_list = findViewById(R.id.author_name_list);
        author_img = findViewById(R.id.author_image_represent);

        Bundle getInf = getIntent().getExtras();
        author_name.setText(getInf.getString("author_name"));
        author_des.setText(getInf.getString("author_description"));
        email = getInf.getString("email");
        username = getInf.getString("username");
        point = getInf.getString("point");
        Glide.with(author_img.getContext()).load(getInf.getString("author_img")).into(author_img);

        //Load nhung dau sach thuoc tac gia nay
        OpenBook();
        list = new ArrayList<>();
        Homepage_recommend_adapter adapter = new Homepage_recommend_adapter(listenner,list);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sach");
        databaseReference.orderByChild("book_author").equalTo(author_name.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Book_overal book = dataSnapshot.getValue(Book_overal.class);
                    list.add(book);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        author_list.setLayoutManager(new GridLayoutManager(this,3));
        author_list.setItemAnimator(new DefaultItemAnimator());
        author_list.setAdapter(adapter);
    }

    private void OpenBook() {
        listenner = new Homepage_recommend_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {

                //Lay thong tin nguoi dung
                DatabaseReference acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                acc_infor_ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String types = "",points = "";
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                            types = infor.getAcc_type();
                            points = infor.getPoint();
                        }

                        Intent i = new Intent(AuthorPageDetail.this,BookDetailPage.class);
                        i.putExtra("book_name",list.get(position).getBook_name());
                        i.putExtra("email",email);
                        i.putExtra("types",types);
                        i.putExtra("point",points);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        };
    }
}