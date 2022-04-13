package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Leaderboardpage_leaderboard_detail_adapter extends RecyclerView.Adapter<Leaderboardpage_leaderboard_detail_adapter.MyViewHolder> {

    private ArrayList<Book_overal> list;
    RecyclerviewClick click;

    public Leaderboardpage_leaderboard_detail_adapter(ArrayList<Book_overal> list,RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_leaderboardpage_leaderboard_detail,parent,false);

        return new Leaderboardpage_leaderboard_detail_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String text = list.get(position).getBook_name();
        String new_name = "";
        if(text.length() > 15){
            new_name = text.substring(0,15)+"...".toString();
            holder.book_name.setText(new_name);
        }else{
            holder.book_name.setText(text);
        }

        holder.book_author.setText(list.get(position).getBook_author());
        holder.book_watch.setText(list.get(position).getBook_watch());
        holder.rank.setText("#"+(position + 1));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sach");
        reference.orderByChild("book_name").equalTo(text).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data_img = "";
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    data_img = overal.getBook_img();
                }
                Glide.with(holder.img.getContext()).load(data_img).into(holder.img);
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

        TextView book_name,book_author,book_watch,rank;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.leaderboard_detail_book_name);
            book_author = itemView.findViewById(R.id.leaderboard_detail_book_author);
            book_watch = itemView.findViewById(R.id.leaderboard_detail_book_watch);
            rank = itemView.findViewById(R.id.leaderboard_detail_rank);
            img = itemView.findViewById(R.id.leaderboard_detail_book_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onOpenThisBook(v,getAdapterPosition());
                }
            });
        }

    }

    public interface RecyclerviewClick{
        void onOpenThisBook(View v, int position);
    }
}
