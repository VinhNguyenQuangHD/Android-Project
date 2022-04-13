package com.example.myandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Lead_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Lead_frag extends Fragment {

    private RecyclerView recyclerView, recyclerview2;
    private Leaderboardpage_top_3_best_adapter adapter;
    private ArrayList<Book_overal> list,list2;

    String get_emails,user;

    private Leaderboardpage_leaderboard_detail_adapter adapter2;

    Leaderboardpage_leaderboard_detail_adapter.RecyclerviewClick click;
    Leaderboardpage_top_3_best_adapter.RecyclerviewClick click2;

    public Lead_frag() {
        // Required empty public constructor
    }

    public static Lead_frag newInstance(String param1, String param2) {
        Lead_frag fragment = new Lead_frag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lead_frag, container, false);

        recyclerview2 = (RecyclerView)view.findViewById(R.id.leaderboard_detail_view) ;
        recyclerView = (RecyclerView) view.findViewById(R.id.top_3_best_view);

        Bundle get_bundle = getActivity().getIntent().getExtras();
        get_emails = get_bundle.getString("email");




        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("sach");

            databaseReference.orderByChild("book_watch")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Book_overal book = snapshot.getValue(Book_overal.class);
                            if (book!=null){
                                list.add(0,book);
                                adapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        OpenBook();
        adapter2 = new Leaderboardpage_leaderboard_detail_adapter(list,click);
        recyclerview2.setAdapter(adapter2);
        adapter = new Leaderboardpage_top_3_best_adapter(list,click2);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void OpenBook() {
        click = new Leaderboardpage_leaderboard_detail_adapter.RecyclerviewClick() {
            @Override
            public void onOpenThisBook(View v, int position) {
                DatabaseReference acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                acc_infor_ref.orderByChild("email").equalTo(get_emails).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String point = "",types = "";
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor account = dataSnapshot.getValue(Account_infor.class);
                            point = account.getPoint();
                            types = account.getAcc_type();
                        }

                        Intent i = new Intent(getContext(),BookDetailPage.class);
                        i.putExtra("book_name",list.get(position).getBook_name());
                        i.putExtra("email",get_emails);
                        i.putExtra("types",types);
                        i.putExtra("point",point);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };

        click2 = new Leaderboardpage_top_3_best_adapter.RecyclerviewClick() {
            @Override
            public void onClick(View v, int position) {
                DatabaseReference acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                acc_infor_ref.orderByChild("email").equalTo(get_emails).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String point = "",types = "";
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Account_infor account = dataSnapshot.getValue(Account_infor.class);
                            point = account.getPoint();
                            types = account.getAcc_type();
                        }

                        Intent i = new Intent(getContext(),BookDetailPage.class);
                        i.putExtra("book_name",list.get(position).getBook_name());
                        i.putExtra("email",get_emails);
                        i.putExtra("types",types);
                        i.putExtra("point",point);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };

    }

    private void GetUser(String email) {
        ArrayList<Account> acc_list = new ArrayList<>();

        DatabaseReference acc_ref = FirebaseDatabase.getInstance().getReference("taikhoan");
        DatabaseReference acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        acc_ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Account account = dataSnapshot.getValue(Account.class);
                    acc_list.add(account);

                    user = acc_list.get(0).getUsername();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}