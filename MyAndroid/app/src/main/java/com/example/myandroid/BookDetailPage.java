package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class BookDetailPage extends AppCompatActivity{

    private ArrayList<Book_overal> list;
    private ArrayList<Friend_req> friend_lists;
    private ArrayList<Library> list_lib;
    ArrayList<Comment_report> comment_list;

    private RecyclerView commend_view;
    private RecyclerView same_type_view;

    CardView frame,frame2;

    Homepage_recommend_adapter.RecyclerviewClick get_listen;
    BookDetailPage_friend_list_adapter.RecyclerviewClick listen_even;
    BookDetailPage_load_all_lib_adapter.RecyclerviewClick add_to_lib;
    BookDetailPage_commend_adapter.RecyclerviewClick commend_listen;

    TextView textView,textView2,textView3, rank_point_view, text_description,points_gaints;
    EditText editText;
    Button send_comment, buy_book, open_book_chapter;
    ImageButton report_book_btn;
    ImageButton favorite_library_btn;
    ImageButton add_to_lib_btn;
    ImageButton rating_btn;
    ImageButton more_option;

    ImageView book_img_view;
    Spinner spinner;

    String user, get_friend_email, get_book_src, rank_point, user_types,book_types;
    Bundle getExtra;

    Button buy_for_friend,tell_friend;

    int point ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail_page);

        //ImageView
        book_img_view = findViewById(R.id.image_book_display);

        //Recycler View
        commend_view = findViewById(R.id.comment_section_area);
        same_type_view = findViewById(R.id.same_type_list);

        //TextView
        textView = findViewById(R.id.detail_book_name);
        textView2 = findViewById(R.id.detail_author_name);
        textView3 = findViewById(R.id.detail_type_txt);
        text_description = findViewById(R.id.detail_description_book_display_text);
        points_gaints = findViewById(R.id.points_gaints);

        //Button
        send_comment = findViewById(R.id.comment_add_btn);
        add_to_lib_btn = findViewById(R.id.bookdetailpage_get_more_option);
        buy_book = findViewById(R.id.spend_point_btn);
        report_book_btn = findViewById(R.id.bookdetailpage_report_btn);
        rating_btn = findViewById(R.id.select_rank_btn);
        more_option = findViewById(R.id.bookdetailpage_get_more_option);
        open_book_chapter = findViewById(R.id.open_book_onliner);

        //Spinner
        spinner = findViewById(R.id.select_rank);

        ///ImageView
        rank_point_view = findViewById(R.id.bookdetailpage_total_rank_point);
        favorite_library_btn = findViewById(R.id.bookdetailpage_favorite_list);

        //Edit text
        editText = findViewById(R.id.bookdetail_book_commend);

        getExtra = getIntent().getExtras();
        textView.setText(getExtra.getString("book_name"));
        user = getExtra.getString("email");
        user_types = getExtra.getString("types");
        book_types = textView3.getText().toString();
        final String[] watch = {""};

        //Mo bao cao dau sach
        report_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportStage(Gravity.CENTER);
            }
        });


        //Mo bang chon diem
        ArrayAdapter<CharSequence> char_adapter = ArrayAdapter.createFromResource(this,R.array.bookdetail_spiner,R.layout.spinner_drop_down_item);
        char_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(char_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String points = parent.getItemAtPosition(position).toString();
                            rank_point = points;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        //Danh gia dau sach
        rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("danhgia");
                databaseRef.child(user.replace(".","")+textView.getText().toString())
                        .setValue(new Ranking(user,textView.getText().toString(), rank_point));

                Toast.makeText(BookDetailPage.this, "Cảm ơn bạn đã đánh giá", Toast.LENGTH_SHORT).show();
            }
        });

        //Lay diem danh gia
        ArrayList<Ranking> rank_list = new ArrayList<>();
        DatabaseReference rank_ref = FirebaseDatabase.getInstance().getReference("danhgia");
        rank_ref.orderByChild("book_name").equalTo(textView.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rank_list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Ranking rank = dataSnapshot.getValue(Ranking.class);
                    rank_list.add(rank);
                }
                double sum = 0;
                for(int i = 0; i < rank_list.size();i++){
                    sum = sum + Integer.parseInt(rank_list.get(i).getRank_point());
                }
                rank_point_view.setText(String.valueOf(sum / rank_list.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Lay thong tin sach
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("binhluan");

        //Hien thi dau sach duoc chon
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sach");
        reference.orderByChild("book_name").equalTo(textView.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    textView2.setText(overal.getBook_author());
                    textView3.setText(overal.getBook_type());
                    Glide.with(book_img_view.getContext()).load(overal.getBook_img()).into(book_img_view);
                    text_description.setText(overal.getBook_description());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Lay so diem can mua
        DatabaseReference set_point_ref = FirebaseDatabase.getInstance().getReference().child("sach");
        DatabaseReference account_check_ref = FirebaseDatabase.getInstance().getReference().child("thongtintaikhoan");
        set_point_ref.orderByChild("book_name").equalTo(textView.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String book_point = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal detail = dataSnapshot.getValue(Book_overal.class);
                    book_point = detail.getBook_point();
                    get_book_src = detail.getBook_src();
                    watch[0] = detail.getBook_watch();

                }
                String book_points = book_point;
                account_check_ref.orderByChild("email").equalTo(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String types = "";
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                            types = infor.getAcc_type();
                        }
                        if(types.equals("VIP")){
                            point = (int) (Integer.parseInt(book_points)
                                    - (Integer.parseInt(book_points)*0.25));
                            buy_book.setText(String.valueOf(point));
                        }else
                        {
                            buy_book.setText(book_points);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Mo chuong sach
        open_book_chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap get_point = new HashMap();
                get_point.put("point","1500");
                DatabaseReference user_ref2 = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                user_ref2.child(getExtra.getString("email").replace(".","")).updateChildren(get_point).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BookDetailPage.this, "Cảm ơn bạn đã đọc đầu sách này", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent i = new Intent(BookDetailPage.this,ChapterDetailPage.class);
                i.putExtra("src",get_book_src);
                i.putExtra("book_chapter","chuong sach");
                i.putExtra("book_name",textView.getText().toString());
                i.putExtra("email",user);
                startActivity(i);
            }
        });

        //Binh luan
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.push().setValue(new Comment_report(user,textView.getText().toString(),editText.getText().toString(),"1",user));
                Toast.makeText(getApplicationContext(), "Bạn đã bình luận", Toast.LENGTH_SHORT).show();
            }
        });

        //Them vao thu vien yeu thich
        favorite_library_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("thuvienyeuthich");
                databaseReference.child(user.replace(".","")+textView.getText().toString()).setValue(new Favorite_book(user,textView.getText().toString(),textView2.getText().toString(),textView3.getText().toString()));
                Toast.makeText(getApplicationContext(), "Bạn đã thêm vào danh sách ưa thích", Toast.LENGTH_SHORT).show();
            }
        });

        //Mua sach
        buy_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int book_price = Integer.parseInt(buy_book.getText().toString());
                String get_point = getExtra.getString("point");
                int user_point = Integer.parseInt(get_point);
                int last_point = user_point - book_price;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chitietthuvien");
                databaseReference.orderByChild("email").equalTo(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String book_name = "";
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Lib_detail detail = dataSnapshot.getValue(Lib_detail.class);
                            book_name = detail.getBook_name();
                        }
                        if(book_name.equals(textView.getText().toString())){
                            Toast.makeText(BookDetailPage.this, "Bạn đã có đầu sách này trong thư viện", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(book_price > user_point){
                                Toast.makeText(BookDetailPage.this, "Bạn không đủ điểm để mua", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                BuyingEven(last_point,Gravity.CENTER);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        SetCommentArea();
        setSameTypeList();

        //Them mot so lua chon khac
        more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListMoreOption(Gravity.CENTER,watch[0]);
            }
        });


    }

    //Xu ly du kien mua sach
    private void BuyingEven(int last_point, int gravity) {

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
        DisplayLibEven(view,last_point);

        custom_dialog.show();
    }

    //Hien thi thu vien cung bang chon
    private void DisplayLibEven(RecyclerView view, int last_point) {
        list_lib = new ArrayList<>();

        LibEven(last_point);
        BookDetailPage_load_all_lib_adapter adapter = new BookDetailPage_load_all_lib_adapter(list_lib,add_to_lib);
        DatabaseReference lib_list_ref = FirebaseDatabase.getInstance().getReference("thuvien");
        lib_list_ref.orderByChild("email").equalTo(user).addValueEventListener(new ValueEventListener() {
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
    private void LibEven(int last_point) {
        add_to_lib = new BookDetailPage_load_all_lib_adapter.RecyclerviewClick() {
            @Override
            public void AddToLib(View v, int position) {
                DatabaseReference add_to_lib_ref = FirebaseDatabase.getInstance().getReference("chitietthuvien");
                Lib_detail detail = new Lib_detail(list_lib.get(position).getLib_name(),textView.getText().toString(),textView2.getText().toString(),"none",user);

                add_to_lib_ref.push().setValue(detail);

                int buy_price = Integer.parseInt(buy_book.getText().toString());

                //Cap nhat so du
                HashMap points = new HashMap();
                points.put("point",String.valueOf(last_point));
                DatabaseReference points_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                points_ref.child(user.replace(".","")).updateChildren(points).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BookDetailPage.this, "Bạn đã mua đầu sách này", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

    }

    //Mo bang chon them mot so lua chon khac
    private void ListMoreOption(int gravity, String watch) {
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_bookdetaillpage_more_option);

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

        frame = custom_dialog.findViewById(R.id.frames);
        frame2 = custom_dialog.findViewById(R.id.frames_2);

        buy_for_friend = custom_dialog.findViewById(R.id.buy_for_friend_btn);
        Button gift_for_friend = custom_dialog.findViewById(R.id.send_for_friend_btn);
        tell_friend = custom_dialog.findViewById(R.id.recommend_for_friend_btn);

        RecyclerView recyclerView = custom_dialog.findViewById(R.id.friendlist_to_choose);

        //Mua cho ban be
        int book_price = Integer.parseInt(buy_book.getText().toString());
        String get_point = getExtra.getString("point");
        int user_point = Integer.parseInt(get_point);

        //Tai len danh sach ban be
        friend_lists = new ArrayList<>();
        GetFriendInfor();

        BookDetailPage_friend_list_adapter adapter = new BookDetailPage_friend_list_adapter(friend_lists,listen_even);
        DatabaseReference friend_list = FirebaseDatabase.getInstance().getReference("banbe");
        friend_list.orderByChild("email_send").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend_lists.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend_req friend = dataSnapshot.getValue(Friend_req.class);
                    friend_lists.add(friend);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        gift_for_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });
        custom_dialog.show();
    }

    //Lay thong tin ban be
    private void GetFriendInfor() {
        listen_even = new BookDetailPage_friend_list_adapter.RecyclerviewClick() {
            @Override
            public void CancelRequest(View v, int position) {
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.GONE);
                get_friend_email = "";
            }

            @Override
            public void ConfirmRequest(View v, int position) {
                frame.setVisibility(View.VISIBLE);
                frame2.setVisibility(View.VISIBLE);

                DatabaseReference friend_email = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                friend_email.orderByChild("email").equalTo(friend_lists.get(position).getEmail()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor acc = dataSnapshot.getValue(Account_infor.class);
                            get_friend_email = acc.getEmail();
                        }

                        //Thong bao den ban be
                        tell_friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NoticeToFriends(user,get_friend_email,"Bạn đã được tặng đầu sách:" + textView.getText().toString());
                                Toast.makeText(BookDetailPage.this, "Bạn đã thông báo cho bạn bè", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //Tang sach cho ban be
                        buy_for_friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BuyForFriend();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
    }

    //Thong bao den nguoi nhan
    private void NoticeToFriend(String from,String to,String content) {
        DatabaseReference notice = FirebaseDatabase.getInstance().getReference("quatang");
        GiftReceiver notices = new GiftReceiver(from,to,content,"1");
        notice.push().setValue(notices);
    }

    private void NoticeToFriends(String from,String to,String content) {
        DatabaseReference notice = FirebaseDatabase.getInstance().getReference("thongbao");
        Notification notices = new Notification(from,to,content,"1");
        notice.push().setValue(notices);
    }

    //Them nhung dau sach cung the loai
    private void setSameTypeList() {
        list = new ArrayList<>();

        Sametype_open();

        Homepage_recommend_adapter adapter = new Homepage_recommend_adapter(get_listen,list);
        DatabaseReference same_type_ref = FirebaseDatabase.getInstance().getReference("sach");
        same_type_ref.orderByChild("book_name").equalTo(textView.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String book_types = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    book_types = overal.getBook_type();
                }

                same_type_ref.orderByChild("book_type").equalTo(book_types).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                            if(!overal.getBook_name().equals(textView.getText().toString())){
                                list.add(overal);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        same_type_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        same_type_view.setItemAnimator(new DefaultItemAnimator());
        same_type_view.setAdapter(adapter);

    }

    private void Sametype_open() {
        get_listen = new Homepage_recommend_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {
                Intent i = new Intent(BookDetailPage.this,BookDetailPage.class);
                i.putExtra("book_name",list.get(position).getBook_name());
                i.putExtra("email",user);
                i.putExtra("types",user_types);
                i.putExtra("point",getExtra.getString("point"));
                startActivity(i);
            }
        };
    }

    //Load toan bo binh luan ve dau sach nay
    public void SetCommentArea(){
        comment_list = new ArrayList<>();

        AddFriendAndReport();
        BookDetailPage_commend_adapter comment_adapter = new BookDetailPage_commend_adapter(comment_list,commend_listen);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("binhluan");
        databaseReference.orderByChild("book_name").equalTo(textView.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comment_list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                   Comment_report comment_class = dataSnapshot.getValue(Comment_report.class);
                   comment_list.add(comment_class);
                   comment_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        commend_view.setLayoutManager(new LinearLayoutManager(this));
        commend_view.setItemAnimator(new DefaultItemAnimator());
        commend_view.setAdapter(comment_adapter);
    }

    private void AddFriendAndReport() {
        commend_listen = new BookDetailPage_commend_adapter.RecyclerviewClick() {
            @Override
            public void AddNewFriend(View v, int position) {
                String get_comment_email = comment_list.get(position).getEmail();

                if(get_comment_email.equals(user)){
                    Toast.makeText(BookDetailPage.this, "Đây là bình luận của bạn", Toast.LENGTH_SHORT).show();
                }else{
                    //Gui yeu cau ket ban den nguoi kia
                    DatabaseReference friend = FirebaseDatabase.getInstance().getReference("ketban");
                    Notification notification = new Notification(user,get_comment_email,"","1");
                    friend.child(user.replace(".","")).setValue(notification);
                }
            }

            @Override
            public void ReportThisCommend(View v, int position) {
                String get_comment_email = comment_list.get(position).getEmail();
                if(get_comment_email.equals(user)){
                    Toast.makeText(BookDetailPage.this, "Đây là bình luận của bạn", Toast.LENGTH_SHORT).show();
                }else{
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("baocao");
                databaseRef.child(user.replace(".","")+get_comment_email.replace(".","")).setValue(new Comment_report(user,get_comment_email,"Bình luận này không phù hợp","2","null"));

                Toast.makeText(BookDetailPage.this, "Cảm ơn bạn đã báo cáo", Toast.LENGTH_SHORT).show();}
            }
        };
    }

    //Bao cao dau sach
    private void ReportStage(int gravity){
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_book_detailpage_report);

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

        Button send_report = custom_dialog.findViewById(R.id.report_send_btn);
        Button close_report = custom_dialog.findViewById(R.id.report_send_close_btn);
        Spinner spinner2 = custom_dialog.findViewById(R.id.bookdetail_report_choose);

        //Dong bao cao dau sach
        close_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

        final String[] report_content = {""};

        //Gui bao cao dau sach
        ArrayAdapter<CharSequence> char_adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),R.array.bookdetail_spiner_report,R.layout.spinner_drop_down_item);
        char_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(char_adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                report_content[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("baocao");
                databaseRef.child(user.replace(".","")+textView.getText().toString()).setValue(new Comment_report(user,textView.getText().toString(), report_content[0],"2","null"));

                Toast.makeText(BookDetailPage.this, "Cảm ơn bạn đã báo cáo", Toast.LENGTH_SHORT).show();
                custom_dialog.dismiss();
            }
        });

        custom_dialog.show();
    }

    //Mua cho ban be
    private void BuyForFriend(){
        int price = Integer.parseInt(buy_book.getText().toString());
        String point = getExtra.getString("point");
        int owner_point = Integer.parseInt(point);

        if(price > owner_point){
            Toast.makeText(BookDetailPage.this, "Bạn không đủ điểm để mua", Toast.LENGTH_SHORT).show();
        }else{
            //Xet diem sau khi mua
            int remain_point = owner_point - price;
            HashMap changes_point = new HashMap();
            changes_point.put("point",String.valueOf(remain_point));

            //Thong bao voi ban be
            NoticeToFriend(user,get_friend_email,textView.getText().toString());
            Toast.makeText(BookDetailPage.this, "Đã tặng đầu sách cho bạn bè", Toast.LENGTH_SHORT).show();
        }
    }
}