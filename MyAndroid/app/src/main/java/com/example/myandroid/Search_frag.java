package com.example.myandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search_frag extends Fragment {


    private ArrayList<Book_overal> list;

    private Homepage_recommend_adapter adapter;
    private Homepage_recommend_adapter.RecyclerviewClick listenner;

    RecyclerView recyclerView;
    EditText searching_text;
    ImageButton search_btn;
    TextView not_result;

    ImageButton work_topic,adventure_topic,science_topic,
            fiction_topic,life_topic,love_topic,horror_topic,detective_topic;
    public Search_frag() {
        // Required empty public constructor
    }

    public static Search_frag newInstance(String param1, String param2) {
        Search_frag fragment = new Search_frag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_frag, container, false);

        recyclerView = v.findViewById(R.id.searchfrag_search_result);
        searching_text = v.findViewById(R.id.search_text);
        not_result = v.findViewById(R.id.null_search_result);
        search_btn = v.findViewById(R.id.search_result_btn);

        work_topic = v.findViewById(R.id.topic_work_image);
        adventure_topic = v.findViewById(R.id.topic_adventure_image);
        science_topic = v.findViewById(R.id.topic_science_image);
        fiction_topic = v.findViewById(R.id.topic_fiction_image);
        life_topic = v.findViewById(R.id.topic_life_image);
        love_topic = v.findViewById(R.id.topic_romantic_image);
        horror_topic = v.findViewById(R.id.topic_horror_image);
        detective_topic = v.findViewById(R.id.topic_detective_image);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchingProgress();
            }
        });

        work_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Công việc";
                String img_title = "1";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image",img_title);
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        adventure_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Phiêu lưu";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","2");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        science_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Khoa học";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","3");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        fiction_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Viễn tưởng";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","4");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        life_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Cuộc sống";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","5");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        love_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Lãng mạn";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","6");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        horror_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Kinh dị";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","7");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        detective_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Trinh thám";
                Intent intent = new Intent(getContext(),TopicDiscursion.class);
                intent.putExtra("image","8");
                intent.putExtra("title",text);
                startActivity(intent);
            }
        });

        return v;
    }

    private void SearchingProgress() {
        not_result.setVisibility(View.GONE);
        String search_request = searching_text.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sach");
        ref.orderByChild("book_name").startAt(search_request).endAt(search_request + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    list.add(overal);
                    adapter.notifyDataSetChanged();
                }
                if(list.size() == 0)
                {
                    not_result.setVisibility(View.VISIBLE);
                }
                else
                {
                    not_result.setVisibility(View.GONE);
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
                Intent i = new Intent(getContext(),BookDetailPage.class);
                i.putExtra("book_name",list.get(position).getBook_name());
                i.putExtra("book_author",list.get(position).getBook_author());
                i.putExtra("book_watch",list.get(position).getBook_watch());
                startActivity(i);
            }
        };
    }


}