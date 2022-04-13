package com.example.myandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_frag extends Fragment {

    private ArrayList<Book_overal> list,new_list,topic_list;
    private ArrayList<Account> acc_list;

    private RecyclerView recycleview, what_new_recyclerview;

    private Homepage_recommend_adapter adapter;
    private Homepage_recommend_adapter.RecyclerviewClick listenner,topic_adapter;

    private Homepage_what_new_adapter what_new_adapter;
    private Homepage_what_new_adapter.RecyclerviewClick what_new_listenner;

    private ImageButton side_menu, daily_reward_btn;

    private CircleImageView user_img;

    private TextView textView,point_view;

    String email_txt,type_txt;

    public Home_frag() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static Home_frag newInstance() {
        Home_frag fragment = new Home_frag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_frag, container, false);

        recycleview = (RecyclerView) view.findViewById(R.id.recomend_listzzz);
        side_menu = (ImageButton) view.findViewById(R.id.side_menu_button);
        recycleview.setLayoutManager(new GridLayoutManager(getContext(),3));

        textView = (TextView) view.findViewById(R.id.homepage_user_txt);
        point_view = (TextView) view.findViewById(R.id.homepage_user_point_txt);

        daily_reward_btn = (ImageButton) view.findViewById(R.id.homepage_daily_login);

        what_new_recyclerview = (RecyclerView) view.findViewById(R.id.bookpage_datetime_new_book);
        what_new_recyclerview.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        user_img = (CircleImageView) view.findViewById(R.id.user_img_display);


        list = new ArrayList<>();
        acc_list = new ArrayList<>();

        Bundle bundle = getActivity().getIntent().getExtras();
        email_txt = bundle.getString("email");
        type_txt = bundle.getString("type");


        //Lay thong tin nguoi dung
        GetUser(email_txt);
        String user_text = textView.getText().toString();
        if(user_text.length() > 10)
        {
            textView.setText(user_text.substring(0,6)+"...");
        }

        //Mo menu
        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),OptionPage.class);
                i.putExtra("emails",email_txt);
                i.putExtra("username",textView.getText().toString());
                i.putExtra("types",type_txt);
                i.putExtra("point",point_view.getText().toString());
                startActivity(i);
            }
        });
        //Load danh sach khuyen nghi
        Random rand = new Random();
        int startPrice = (int) 1200 + (rand.nextInt(1000)+1);
        int endPrice = (int) 3000 + (rand.nextInt(1000)+1);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sach");
        ref.orderByChild("book_point").startAt(String.valueOf(startPrice)).endAt(String.valueOf(endPrice)).addValueEventListener(new ValueEventListener() {
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

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);


        String get_today = day+"/"+month+"/"+year;
        String get_pre_day = (day-6)+"/"+month+"/"+year;

        new_list = new ArrayList<>();
        DatabaseReference what_new_ref = FirebaseDatabase.getInstance().getReference("sach");
        what_new_ref.orderByChild("book_date").startAt(get_pre_day).endAt(get_today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    if(!overal.getBook_type().equals("VIP")){
                        new_list.add(overal);
                    }
                    what_new_adapter.notifyDataSetChanged();

                    if(new_list.size() == 6){
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        What_new_setOnClickListenner();
        what_new_adapter = new Homepage_what_new_adapter(new_list,what_new_listenner);
        what_new_recyclerview.setAdapter(what_new_adapter);

        setOnClickListenner();
        adapter = new Homepage_recommend_adapter(listenner,list);
        recycleview.setAdapter(adapter);

        //Diem danh dang nhap
        daily_reward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEvent(Gravity.CENTER);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void OpenTopicList() {
        topic_adapter = new Homepage_recommend_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {
                if(topic_list.get(position).getBook_type().equals("VIP") && !type_txt.equals("VIP")){
                    Toast.makeText(getContext(), "Đầu sách chỉ dành cho thành viên VIP", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(getContext(),BookDetailPage.class);
                    i.putExtra("book_name",topic_list.get(position).getBook_name());
                    i.putExtra("email",email_txt);
                    i.putExtra("types",type_txt);
                    i.putExtra("point",point_view.getText().toString());
                    startActivity(i);
                }

            }
        };
    }

    //Phan thuong moi ngay
    private boolean LoginState() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);


        String get_today = year+""+month+""+day;

        boolean isDailyClaim = false;

        SharedPreferences shared = getActivity().getSharedPreferences("PRE1",0);

        boolean isToday = shared.getBoolean(get_today,false);

        if(!isToday){

            SharedPreferences.Editor edit = shared.edit();
            edit.putBoolean(get_today,true);
            edit.apply();

            return true;
        }else{
            return false;
        }
    }

    //Diem danh nhan qua
    private void LoginEvent(int gravity){
        final Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_homepage_daily_login);

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

        Button Claim_reward = custom_dialog.findViewById(R.id.daily_login_btn);

        TextView point = custom_dialog.findViewById(R.id.daily_login_point);
        TextView notice = custom_dialog.findViewById(R.id.daily_login_notice);

        String get_current_point = point_view.getText().toString();
        String add_new_point = point.getText().toString();

        Claim_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginState()==true){

                    int new_point = Integer.parseInt(get_current_point.replace(" point",""))
                            + Integer.parseInt(add_new_point.replace(" point",""));

                    HashMap point_change = new HashMap();
                    point_change.put("point",String.valueOf(new_point));

                    DatabaseReference login_new = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                    login_new.child(email_txt.replace(".",""))
                            .updateChildren(point_change).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Đã thay đổi điểm", Toast.LENGTH_SHORT).show();
                                custom_dialog.dismiss();
                            }
                        }
                    });
                }else
                {
                    Toast.makeText(getContext(), "Hãy quay lại vào ngày mai", Toast.LENGTH_SHORT).show();
                    custom_dialog.dismiss();
                }
            }
        });
        custom_dialog.show();
    }

    //Load nhung dau sach moi
    private void What_new_setOnClickListenner() {
        what_new_listenner = new Homepage_what_new_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {
                Intent i = new Intent(getContext(),BookDetailPage.class);
                i.putExtra("book_name",new_list.get(position).getBook_name());
                i.putExtra("email",email_txt);
                i.putExtra("types",type_txt);
                i.putExtra("point",point_view.getText().toString());
                startActivity(i);
            }
        };
    }

    //Lay thong tin nguoi dung
    private void GetUser(String email) {
        DatabaseReference acc_ref = FirebaseDatabase.getInstance().getReference("taikhoan");
        DatabaseReference acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        acc_ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Account account = dataSnapshot.getValue(Account.class);
                    acc_list.add(account);

                    textView.setText(acc_list.get(0).getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        acc_infor_ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    point_view.setText(infor.getPoint());
                    Glide.with(user_img.getContext()).load(infor.getImgsrc()).into(user_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnClickListenner() {
        listenner = new Homepage_recommend_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {
                if(list.get(position).getBook_type().equals("VIP") && !type_txt.equals("VIP")){
                    Toast.makeText(getContext(), "Đầu sách chỉ dành cho thành viên VIP", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent i = new Intent(getContext(),BookDetailPage.class);
                i.putExtra("book_name",list.get(position).getBook_name());
                i.putExtra("email",email_txt);
                i.putExtra("types",type_txt);
                i.putExtra("point",point_view.getText().toString());
                startActivity(i);}
            }
        };
    }

}