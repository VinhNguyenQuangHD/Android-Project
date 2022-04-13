package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryDetailPage_book_list_adapter extends RecyclerView.Adapter<LibraryDetailPage_book_list_adapter.MyViewHolder> {

    ArrayList<Lib_detail> list;
    RecyclerviewClick listenner;

    public LibraryDetailPage_book_list_adapter(ArrayList<Lib_detail> list, RecyclerviewClick listenner) {
        this.list = list;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_libdetailpage_book_list,parent,false);
        return new LibraryDetailPage_book_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String text = list.get(position).getBook_name();
        String new_text="";
        if(text.length() > 18){
            new_text = text.substring(0,17) + "...";
            holder.text.setText(new_text);
        }else
        {
            holder.text.setText(text);
        }

        holder.text2.setText(list.get(position).getBook_author());

        DatabaseReference img_load = FirebaseDatabase.getInstance().getReference("sach");
        img_load.orderByChild("book_name").equalTo(list.get(position).getBook_name()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img_link = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    img_link = overal.getBook_img();
                }
                Glide.with(holder.img.getContext()).load(img_link).into(holder.img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,text2;
        private ImageView img;
        private ImageButton download_btn, send_for_friend, delete_lib_book;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.libdetail_book_name);
            text2 = itemView.findViewById(R.id.libdetail_book_author);
            img = itemView.findViewById(R.id.libdetail_book_img);
            download_btn = itemView.findViewById(R.id.download_book_name_btn);
            send_for_friend = itemView.findViewById(R.id.sending_book_for_friend_name_btn);
            delete_lib_book = itemView.findViewById(R.id.delete_book_from_lib_btn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.OpenBook(v,getAdapterPosition());
                }
            });

            download_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickDownload(v,getAdapterPosition());
                }
            });

            send_for_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listenner.SendForFriend(v,getAdapterPosition());
                }
            });

            delete_lib_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.DeleteBook(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void onClickDownload(View v, int position);
        void SendForFriend(View v, int position);
        void OpenBook(View v,int position);
        void DeleteBook(View v,int position);
    }
}
