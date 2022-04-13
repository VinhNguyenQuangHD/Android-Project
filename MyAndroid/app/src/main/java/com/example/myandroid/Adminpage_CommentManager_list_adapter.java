package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adminpage_CommentManager_list_adapter extends RecyclerView.Adapter<Adminpage_CommentManager_list_adapter.MyViewHolder>{
    ArrayList<Comment_report> list;
    RecyclerviewClick listenner;

    public Adminpage_CommentManager_list_adapter(ArrayList<Comment_report> list, RecyclerviewClick listenner) {
        this.list = list;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adminpage_commentmanager_list,parent,false);
        return new Adminpage_CommentManager_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(list.get(position).getUsername());
        holder.book_name.setText(list.get(position).getBook_name());
        holder.content.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username,book_name,content;

        private ImageButton warning_btn,delete_comment_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.admin_comment_manager_username);
            book_name = itemView.findViewById(R.id.admin_comment_manager_book_name);
            content = itemView.findViewById(R.id.admin_comment_manager_content);
            warning_btn = itemView.findViewById(R.id.send_warn_comment_btn);
            delete_comment_btn = itemView.findViewById(R.id.disable_comment_btn);

            warning_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickWarning(v,getAdapterPosition());
                }
            });
            delete_comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickDeleteComment(v,getAdapterPosition());
                }
            });
        }
    }
    public interface RecyclerviewClick{
        void onClickWarning(View v, int position);
        void onClickDeleteComment(View v, int position);
    }
}
