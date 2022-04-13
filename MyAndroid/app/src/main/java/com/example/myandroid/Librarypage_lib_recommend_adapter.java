package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Librarypage_lib_recommend_adapter extends RecyclerView.Adapter<Librarypage_lib_recommend_adapter.MyViewHolder>{

    private ArrayList<Book_overal> recommend_lib;

    public Librarypage_lib_recommend_adapter(ArrayList<Book_overal> recommend_lib) {
        this.recommend_lib = recommend_lib;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,text2,text3;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.lib_book_name);
            text2 = itemView.findViewById(R.id.lib_author_name);
            text3 = itemView.findViewById(R.id.lib_watch_num);
            img = itemView.findViewById(R.id.lib_book_images);
        }
    }

    @NonNull
    @Override
    public Librarypage_lib_recommend_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_librarypage_list_recommend,parent,false);
        return new Librarypage_lib_recommend_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Librarypage_lib_recommend_adapter.MyViewHolder holder, int position) {
        holder.text.setText(recommend_lib.get(position).getBook_name());
        holder.text2.setText(recommend_lib.get(position).getBook_author());
        holder.text3.setText(recommend_lib.get(position).getBook_watch());
        Glide.with(holder.img.getContext()).load(recommend_lib.get(position).getBook_img()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return recommend_lib.size();
    }
}
