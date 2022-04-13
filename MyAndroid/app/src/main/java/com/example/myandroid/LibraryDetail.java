package com.example.myandroid;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryDetail extends AppCompatActivity {

    TextView lib_names,lib_des,user_of_lib,point_of_lib,email_load;
    RecyclerView lib_list;
    LibraryDetailPage_book_list_adapter.RecyclerviewClick listenner, fav_listenner;
    LibraryDetail_sending_friend_item_adapter.RecyclerviewClick friend_listenner;
    String got_user, download_link, get_friend_email;
    int type;
    String type_txt;


    ArrayList<Lib_detail> list;
    ArrayList<Friend_req> friend_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_detail);

        lib_names = findViewById(R.id.lib_name_represent);
        lib_des = findViewById(R.id.lib_description_text);

        lib_list = findViewById(R.id.lib_name_list);
        user_of_lib = findViewById(R.id.user_of_this_lib);
        point_of_lib = findViewById(R.id.point_of_this_lib);
        email_load = findViewById(R.id.lib_email_personal_text);

        Bundle get_infor_from_lib = getIntent().getExtras();
        lib_names.setText(get_infor_from_lib.getString("lib_name"));
        lib_des.setText(get_infor_from_lib.getString("lib_description"));

        type = Integer.parseInt(get_infor_from_lib.getString("lib_type"));
        got_user = get_infor_from_lib.getString("email");
        type_txt = get_infor_from_lib.getString("type");
        email_load.setText(got_user);
        GetUser(got_user);

        if(type == 1){

            list = new ArrayList<>();
            Favolib();
            LibraryDetailPage_book_list_adapter adapter = new LibraryDetailPage_book_list_adapter(list,listenner);

            DatabaseReference favor_ref = FirebaseDatabase.getInstance().getReference("thuvienyeuthich");
            favor_ref.orderByChild("username").equalTo(got_user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Lib_detail lib = dataSnapshot.getValue(Lib_detail.class);
                        list.add(lib);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            lib_list.setLayoutManager(new LinearLayoutManager(this));
            lib_list.setItemAnimator(new DefaultItemAnimator());
            lib_list.setAdapter(adapter);
        }
        else{

            list = new ArrayList<>();

            LibLoad();
            LibraryDetailPage_book_list_adapter adapter = new LibraryDetailPage_book_list_adapter(list,listenner);
            DatabaseReference favor_ref = FirebaseDatabase.getInstance().getReference("chitietthuvien");
            favor_ref.orderByChild("lib_name").equalTo(lib_names.getText().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Lib_detail lib = dataSnapshot.getValue(Lib_detail.class);
                        list.add(lib);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            lib_list.setLayoutManager(new LinearLayoutManager(this));
            lib_list.setItemAnimator(new DefaultItemAnimator());
            lib_list.setAdapter(adapter);
        }
    }

    private void Favolib() {
        listenner = new LibraryDetailPage_book_list_adapter.RecyclerviewClick() {
            @Override
            public void onClickDownload(View v, int position) {
                Toast.makeText(LibraryDetail.this, "Bạn không thể tải xuống đầu sách này", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void SendForFriend(View v, int position) {
                Toast.makeText(LibraryDetail.this, "Bạn không thể gửi cho bạn bè của bạn", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void OpenBook(View v, int position) {
                //Lay thong tin nguoi dung
                DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                user_ref.orderByChild("email").equalTo(got_user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String point = "",types = "";
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                            point = infor.getPoint();
                            types = infor.getAcc_type();
                        }

                        Intent i = new Intent(LibraryDetail.this,BookDetailPage.class);
                        i.putExtra("book_name",list.get(position).getBook_name());
                        i.putExtra("email",email_load.getText().toString());
                        i.putExtra("types",types);
                        i.putExtra("point",point);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void DeleteBook(View v, int position) {
                String key = list.get(position).getBook_name().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("thuvienyeuthich").orderByChild("book_name").equalTo(key);

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
                Toast.makeText(LibraryDetail.this, "Đã xóa khỏi thư viện", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void LibLoad() {
        listenner = new LibraryDetailPage_book_list_adapter.RecyclerviewClick() {
            @Override
            public void onClickDownload(View v, int position) {
                if(type == 1){
                    Toast.makeText(LibraryDetail.this, "Bạn không thể tải đầu sách ", Toast.LENGTH_SHORT).show();
                }else {
                    //Lay link tai dau sach
                    DatabaseReference download_link_ref = FirebaseDatabase.getInstance().getReference("sach");
                    download_link_ref.orderByChild("book_name").equalTo(list.get(position).getBook_name()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                                download_link = overal.getBook_src();
                            }

                            Toast.makeText(LibraryDetail.this, "Bắt đầu tải xuống...", Toast.LENGTH_SHORT).show();
                            //Tai qua link
                            DownloadFile(LibraryDetail.this, download_link, list.get(position).getBook_name(), ".pdf", DIRECTORY_DOWNLOADS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void SendForFriend(View v, int position) {
                SendingFriendEven(Gravity.CENTER, list.get(position).getBook_name(), list.get(position).getBook_author(), download_link);
                //Xoa khoi thu vien cua ban

                /*String key = list.get(position).getBook_name().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("chitietthuvien").orderByChild("book_name").equalTo(key);

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
                Toast.makeText(LibraryDetail.this, "Đã xóa khỏi thư viện", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void OpenBook(View v, int position){
                //Lay thong tin nguoi dung
                DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                user_ref.orderByChild("email").equalTo(got_user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String point = "",types = "";
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                            point = infor.getPoint();
                            types = infor.getAcc_type();
                        }

                        Intent i = new Intent(LibraryDetail.this,BookDetailPage.class);
                        i.putExtra("book_name",list.get(position).getBook_name());
                        i.putExtra("email",email_load.getText().toString());
                        i.putExtra("types",types);
                        i.putExtra("point",point);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void DeleteBook(View v, int position){
                String key = list.get(position).getBook_name().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("chitietthuvien").orderByChild("book_name").equalTo(key);

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
                Toast.makeText(LibraryDetail.this, "Đã xóa khỏi thư viện", Toast.LENGTH_SHORT).show();
            }

        };
    }

    private void SendingFriendEven(int center, String book_name, String book_author, String download_link) {
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
        win_attr.gravity = center;
        window.setAttributes(win_attr);

        if(Gravity.BOTTOM == center){
            custom_dialog.setCancelable(true);
        }else{
            custom_dialog.setCancelable(false);
        }
        TextView text = custom_dialog.findViewById(R.id.bookdetail_load_lib_txt);
        text.setText("Gửi cho bạn bè");
        Button cancel_buying = custom_dialog.findViewById(R.id.close_bookdetail_event);
        cancel_buying.setText("Hủy quà tặng");
        cancel_buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

        RecyclerView view = custom_dialog.findViewById(R.id.bookdetail_library_list_up);

        //Xu ly thu vien
        SendToFriendEven(view,book_name,book_author,download_link);

        custom_dialog.show();
    }

    private void SendToFriendEven(RecyclerView view, String book_name, String book_author, String download_link) {
        friend_list = new ArrayList<>();

        FriendEvent(book_name,book_author,download_link);
        LibraryDetail_sending_friend_item_adapter adapter = new LibraryDetail_sending_friend_item_adapter(friend_list,friend_listenner);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("banbe");
        databaseReference.orderByChild("email_send").equalTo(got_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend_req req = dataSnapshot.getValue(Friend_req.class);
                    friend_list.add(req);
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

    private void FriendEvent(String book_name, String book_author, String download_link) {
        friend_listenner = new LibraryDetail_sending_friend_item_adapter.RecyclerviewClick() {
            @Override
            public void SendingGift(View v, int position) {
                //Lay email ban be
                get_friend_email = friend_list.get(position).getEmail();
                //Thong bao den ban be
                GiftReceiver gift = new GiftReceiver(got_user,get_friend_email,book_name,"1");
                DatabaseReference friend_notice_ref = FirebaseDatabase.getInstance().getReference("quatang");
                friend_notice_ref.push().setValue(gift);
                Toast.makeText(LibraryDetail.this, "Bạn đã tặng cho người bạn của bạ đầu sách này", Toast.LENGTH_SHORT).show();

            }
        };
    }

    private void DownloadFile(Context context,String download_link, String filename,String file_destination,String directory) {
        DownloadManager download = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(download_link);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,directory,filename + file_destination);
        download.enqueue(request);
    }

    private void GetUser(String email) {
        DatabaseReference acc_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        acc_ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Account_infor account = dataSnapshot.getValue(Account_infor.class);

                    user_of_lib.setText(account.getUsername());
                    point_of_lib.setText(account.getPoint());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}