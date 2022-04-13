package com.example.myandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorPage_author_adapter extends RecyclerView.Adapter<AuthorPage_author_adapter.MyViewHolder> {

    ArrayList<Author> list;
    RecycleViewListenner listenner;

    public AuthorPage_author_adapter(ArrayList<Author> list, RecycleViewListenner listenner) {
        this.list = list;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_authorpage_author_list,parent,false);
        return new AuthorPage_author_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.card.setBackgroundColor(Color.TRANSPARENT);
        holder.layout.setBackgroundColor(Color.TRANSPARENT);
        holder.author_name.setTextColor(Color.parseColor("#ffffff"));

        holder.author_name.setText(list.get(position).getAuthor_name());
        Glide.with(holder.img.getContext()).load(list.get(position).getAuthor_img()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView author_name;
        private CircleImageView img;
        private CardView card;
        private RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            author_name = itemView.findViewById(R.id.author_name);
            img = itemView.findViewById(R.id.author_image);
            card = itemView.findViewById(R.id.card_author);
            layout = itemView.findViewById(R.id.author_frame);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.SelectAuthor(v,getAdapterPosition());
                }
            });

        }
    }
    public interface RecycleViewListenner{
        void SelectAuthor(View v, int position);
    }
}
