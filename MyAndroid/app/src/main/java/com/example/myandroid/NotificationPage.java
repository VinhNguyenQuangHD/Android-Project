package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.Random;

public class NotificationPage extends AppCompatActivity {

    RecyclerView newest_list,friend_request_list, gift_recycler;
    ArrayList<Notification> list,normal_list;
    ArrayList<GiftReceiver> gift_list;
    private ArrayList<Library> list_lib;

    NotificationPage_friend_request_adapter.RecyclerviewClick click;
    Notificationpage_normal_notification_adapter.RecyclerviewClick listenner;
    Notificationpage_gift_receiver_adapter.RecyclerviewClick gift_listenner;
    BookDetailPage_load_all_lib_adapter.RecyclerviewClick add_to_lib;

    String own_email,own_username, guest_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);

        newest_list = findViewById(R.id.notification_newest_list);
        friend_request_list = findViewById(R.id.notification_read_list);
        gift_recycler = findViewById(R.id.gift_receive_read_list);

        Bundle bundle = getIntent().getExtras();
        own_email = bundle.getString("email");
        own_username = bundle.getString("username");

        //Nhan thong bao friend_request
        FriendRequestReceive();
        //Nhan thong bao thong dung
        NormalNotification();
        //Nhan qua tang
        GiftNotification();

    }

    private void GiftNotification() {
        gift_list = new ArrayList<>();

        GiftReceiverEven();

        Notificationpage_gift_receiver_adapter adapter = new Notificationpage_gift_receiver_adapter(gift_list,gift_listenner);

        DatabaseReference friend_req_ref = FirebaseDatabase.getInstance().getReference("quatang");
        friend_req_ref.orderByChild("to").equalTo(own_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gift_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GiftReceiver notification = dataSnapshot.getValue(GiftReceiver.class);
                    gift_list.add(notification);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gift_recycler.setLayoutManager(new LinearLayoutManager(this));
        gift_recycler.setItemAnimator(new DefaultItemAnimator());
        gift_recycler.setAdapter(adapter);
    }

    private void GiftReceiverEven() {
        gift_listenner = new Notificationpage_gift_receiver_adapter.RecyclerviewClick() {
            @Override
            public void onClickAdd(View v, int position) {
                BuyingEven(Gravity.CENTER, gift_list.get(position).getContent());
            }
        };
    }

    //Xu ly du kien nhan qua
    private void BuyingEven(int gravity, String content) {

        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.recyclerview_bookdetailpage_library_choose);

        Window window = custom_dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win_attr = window.getAttributes();
        win_attr.gravity = gravity;
        window.setAttributes(win_attr);

        if(Gravity.BOTTOM == gravity){
            custom_dialog.setCancelable(true);
        }else{
            custom_dialog.setCancelable(false);
        }

        Button cancel_buying = custom_dialog.findViewById(R.id.close_bookdetail_event);
        cancel_buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

        RecyclerView view = custom_dialog.findViewById(R.id.bookdetail_library_list_up);

        //Xu ly thu vien
        DisplayLibEven(view,content);

        custom_dialog.show();
    }
    //Hien thi thu vien cung bang chon
    private void DisplayLibEven(RecyclerView view, String content) {
        list_lib = new ArrayList<>();

        LibEven(content);
        BookDetailPage_load_all_lib_adapter adapter = new BookDetailPage_load_all_lib_adapter(list_lib,add_to_lib);
        DatabaseReference lib_list_ref = FirebaseDatabase.getInstance().getReference("thuvien");
        lib_list_ref.orderByChild("email").equalTo(own_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_lib.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Library lib = dataSnapshot.getValue(Library.class);
                    list_lib.add(lib);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view.setLayoutManager(new LinearLayoutManager(this));
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    //Xu ly du kien sau khi load duoc thu vien
    private void LibEven(String content) {
        add_to_lib = new BookDetailPage_load_all_lib_adapter.RecyclerviewClick() {
            @Override
            public void AddToLib(View v, int position) {
                DatabaseReference add_to_lib_ref = FirebaseDatabase.getInstance().getReference("chitietthuvien");
                Lib_detail detail = new Lib_detail(list_lib.get(position).getLib_name(),content,"","none",own_email);

                add_to_lib_ref.push().setValue(detail);
        };

    };}


    private void NormalNotification() {
        normal_list = new ArrayList<>();

        DeleteEvent();

        Notificationpage_normal_notification_adapter adapter = new Notificationpage_normal_notification_adapter(normal_list,listenner);

        DatabaseReference friend_req_ref = FirebaseDatabase.getInstance().getReference("thongbao");
        friend_req_ref.orderByChild("to").equalTo(own_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                normal_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    normal_list.add(notification);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        newest_list.setLayoutManager(new LinearLayoutManager(this));
        newest_list.setItemAnimator(new DefaultItemAnimator());
        newest_list.setAdapter(adapter);
    }

    private void DeleteEvent() {
        listenner = new Notificationpage_normal_notification_adapter.RecyclerviewClick() {
            @Override
            public void onClickDelete(View v, int position) {
                String key = normal_list.get(position).getTo().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("thongbao").orderByChild("to").equalTo(key);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(NotificationPage.this, "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
    }

    private void FriendRequestReceive() {
        list = new ArrayList<>();

        FriendEvent();

        NotificationPage_friend_request_adapter adapter = new NotificationPage_friend_request_adapter(list,click);

        DatabaseReference friend_req_ref = FirebaseDatabase.getInstance().getReference("ketban");
        friend_req_ref.orderByChild("to").equalTo(own_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    list.add(notification);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friend_request_list.setLayoutManager(new LinearLayoutManager(this));
        friend_request_list.setItemAnimator(new DefaultItemAnimator());
        friend_request_list.setAdapter(adapter);


    }


    private void FriendEvent() {
        click = new NotificationPage_friend_request_adapter.RecyclerviewClick() {
            @Override
            public void onClickAccept(View v, int position) {
                //Lay username tu nguoi gui
                DatabaseReference guest_ref = FirebaseDatabase.getInstance().getReference("taikhoan");
                guest_ref.orderByChild("email").equalTo(list.get(position).getFrom()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account acc = dataSnapshot.getValue(Account.class);
                            guest_username = acc.getUsername();
                        }

                        DatabaseReference friend_ref = FirebaseDatabase.getInstance().getReference("banbe");
                        DatabaseReference friend_ref2 = FirebaseDatabase.getInstance().getReference("banbe");

                        Friend_req fr1 = new Friend_req(own_username,list.get(position).getFrom(),own_email);
                        Friend_req fr2 = new Friend_req(guest_username,list.get(position).getTo(),list.get(position).getFrom());

                        friend_ref.push().setValue(fr1);
                        friend_ref2.push().setValue(fr2);

                        Toast.makeText(NotificationPage.this, "Đã thêm vào danh sách bạn bè", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onClickReject(View v, int position) {
                String key = list.get(position).getTo().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("ketban").orderByChild("to").equalTo(key);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(NotificationPage.this, "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
    }
}