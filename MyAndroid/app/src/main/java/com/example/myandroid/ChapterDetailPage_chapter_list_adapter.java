package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChapterDetailPage_chapter_list_adapter extends RecyclerView.Adapter<ChapterDetailPage_chapter_list_adapter.MyViewHolder>{
    ArrayList<Book_chapter> list;
    RecyclerviewClick click;

    public ChapterDetailPage_chapter_list_adapter(ArrayList<Book_chapter> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_bookdetailpage_chapter_list,parent,false);
        return new ChapterDetailPage_chapter_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getBook_chapter());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.book_chapter_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.MoveToChapter(v,getAdapterPosition());
                }
            });
        }
    }
    public interface RecyclerviewClick{
        void MoveToChapter(View v, int position);
    }
}
