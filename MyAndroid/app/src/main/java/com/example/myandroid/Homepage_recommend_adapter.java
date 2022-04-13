package com.example.myandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.Set;

public class Homepage_recommend_adapter extends RecyclerView.Adapter<Homepage_recommend_adapter.MyViewHolder>{//FirebaseRecyclerAdapter<Book_overal,Homepage_recommend_adapter.MyViewHolder>{

    private RecyclerviewClick click;
    private ArrayList<Book_overal> list;

    public Homepage_recommend_adapter(RecyclerviewClick click, ArrayList<Book_overal> list) {
        this.click = click;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_homepage_list_horizon,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.card.setBackgroundColor(Color.TRANSPARENT);
        holder.layout.setBackgroundColor(Color.TRANSPARENT);
        String text = list.get(position).getBook_name();
        String new_name = "";

        if(text.length() > 9){
            new_name = text.substring(0,6)+"...".toString();
            holder.book_name.setText(new_name);
        }else{
            holder.book_name.setText(text);
        }

        holder.book_author.setText(list.get(position).getBook_author());
        Glide.with(holder.img.getContext()).load(list.get(position).getBook_img()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView book_name,book_author;
        ImageView img;
        CardView card;
        RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.book_name);
            book_author = itemView.findViewById(R.id.author_name);
            img = itemView.findViewById(R.id.book_images);
            card = itemView.findViewById(R.id.card_book_frag);
            layout = itemView.findViewById(R.id.book_list_details_homepage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            click.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerviewClick{
        void onClick(View v, int position);
    }
}
