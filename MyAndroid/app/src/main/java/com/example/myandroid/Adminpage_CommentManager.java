package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class Adminpage_CommentManager extends AppCompatActivity {

    RecyclerView comment_view;
    Adminpage_CommentManager_list_adapter.RecyclerviewClick listenner;

    Bundle bundle;
    ArrayList<Comment_report> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_comment_manager);

        comment_view = findViewById(R.id.admin_comment_manager_list);

        bundle = getIntent().getExtras();

        list = new ArrayList<>();

        CommentEvent();
        Adminpage_CommentManager_list_adapter adapter = new Adminpage_CommentManager_list_adapter(list,listenner);
        DatabaseReference acc_list_ref = FirebaseDatabase.getInstance().getReference("binhluan");
        acc_list_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Comment_report infor = dataSnapshot.getValue(Comment_report.class);
                    list.add(infor);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        comment_view.setLayoutManager(new LinearLayoutManager(this));
        comment_view.setItemAnimator(new DefaultItemAnimator());
        comment_view.setAdapter(adapter);
    }

    private void CommentEvent() {
        listenner = new Adminpage_CommentManager_list_adapter.RecyclerviewClick() {
            @Override
            public void onClickWarning(View v, int position) {
                DatabaseReference notification = FirebaseDatabase.getInstance().getReference("thongbao");
                Notification warning_sending = new Notification(bundle.getString("email"),list.get(position).getEmail()
                        ,"Bình luận của bạn đã bị cảnh báo từ phía quản trị viên","2");
                String email = bundle.getString("email");
                notification.child(email.replace(".","")).setValue(warning_sending);

                Toast.makeText(Adminpage_CommentManager.this, "Đã gửi cảnh báo đến người dùng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickDeleteComment(View v, int position) {

                String key = list.get(position).getUsername().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("binhluan").orderByChild("username").equalTo(key);

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

                Toast.makeText(Adminpage_CommentManager.this, "Đã xóa bình luận", Toast.LENGTH_SHORT).show();
            }
        };
    }
}