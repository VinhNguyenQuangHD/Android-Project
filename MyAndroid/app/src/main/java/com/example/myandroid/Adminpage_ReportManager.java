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

import java.util.ArrayList;

public class Adminpage_ReportManager extends AppCompatActivity {

    RecyclerView report_view;
    AdminPage_ReportManager_list_adapter.RecyclerviewClick listen;

    Bundle bundle;
    ArrayList<Comment_report> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_report_manager);

        report_view = findViewById(R.id.admin_report_manager_list);

        bundle = getIntent().getExtras();

        list = new ArrayList<>();

        ReportEvent();
        AdminPage_ReportManager_list_adapter adapter = new AdminPage_ReportManager_list_adapter(list,listen);
        DatabaseReference acc_list_ref = FirebaseDatabase.getInstance().getReference("baocao");
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

        report_view.setLayoutManager(new LinearLayoutManager(this));
        report_view.setItemAnimator(new DefaultItemAnimator());
        report_view.setAdapter(adapter);
    }

    private void ReportEvent() {
        listen = new AdminPage_ReportManager_list_adapter.RecyclerviewClick() {
            @Override
            public void onClickResponse(View v, int position) {
                DatabaseReference notification = FirebaseDatabase.getInstance().getReference("thongbao");
                Notification warning_sending = new Notification(bundle.getString("email"),list.get(position).getEmail()
                        ,"Cảm ơn bạn đã góp ý","2");
                String email = bundle.getString("email");
                notification.child(email.replace(".","")).setValue(warning_sending);

                Toast.makeText(Adminpage_ReportManager.this, "Đã gửi phản hồi đến người dùng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickDeleteReport(View v, int position) {
                String key = list.get(position).getUsername().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("baocao").orderByChild("username").equalTo(key);

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

                Toast.makeText(Adminpage_ReportManager.this, "Đã xóa báo cáo", Toast.LENGTH_SHORT).show();
            }
        };
    }
}