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

public class Leaderboardpage_top_3_best_adapter extends RecyclerView.Adapter<Leaderboardpage_top_3_best_adapter.MyViewHolder> {

    ArrayList<Book_overal> list;
    RecyclerviewClick click;

    public Leaderboardpage_top_3_best_adapter(ArrayList<Book_overal> list,RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_leaderboardpage_top_3_best_board,parent,false);

        return new Leaderboardpage_top_3_best_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String text = list.get(position).getBook_name();
        String new_name = "";
        if(text.length() > 12){
            new_name = text.substring(0,12)+"...".toString();
            holder.book_name.setText(new_name);
        }else{
            holder.book_name.setText(text);
        }

        holder.book_author.setText(list.get(position).getBook_author());
        holder.rank.setText("No."+(position+1));
        holder.book_view.setText(list.get(position).getBook_watch());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sach");
        reference.orderByChild("book_name").equalTo(text).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book_overal overal = dataSnapshot.getValue(Book_overal.class);
                    Glide.with(holder.img.getContext()).load(overal.getBook_img()).into(holder.img);
                }
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

        TextView rank,book_name,book_author,book_view;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.leaderboardpage_top_3_best_book_name);
            book_author = itemView.findViewById(R.id.leaderboardpage_top_3_best_book_author);
            rank = itemView.findViewById(R.id.leaderboardpage_top_3_best_rank);
            book_view = itemView.findViewById(R.id.leaderboardpage_top_3_best_book_watch);
            img = itemView.findViewById(R.id.leaderboardpage_top_3_best_book_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(v,getAdapterPosition());
                }
            });
        }

    }

    public interface RecyclerviewClick{
        void onClick(View v, int position);
    }
}
