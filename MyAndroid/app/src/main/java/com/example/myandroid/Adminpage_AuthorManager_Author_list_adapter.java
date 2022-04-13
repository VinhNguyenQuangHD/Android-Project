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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adminpage_AuthorManager_Author_list_adapter extends RecyclerView.Adapter<Adminpage_AuthorManager_Author_list_adapter.MyViewHolder> {
    ArrayList<Author> list;
    RecyclerviewClick listenner;

    public Adminpage_AuthorManager_Author_list_adapter(ArrayList<Author> list,  RecyclerviewClick listenner) {
        this.list = list;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adminpage_author_manager_author_list,parent,false);
        return new Adminpage_AuthorManager_Author_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getAuthor_name());
        Glide.with(holder.img.getContext()).load(list.get(position).getAuthor_img()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        private CircleImageView img;

        private ImageButton delete_btn,edit_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.adminpage_author_manager_text);
            img = itemView.findViewById(R.id.adminpage_author_manager_img);
            delete_btn = itemView.findViewById(R.id.adminpage_author_manager_delete_author_btn);
            edit_btn = itemView.findViewById(R.id.adminpage_author_manager_edit_author_btn);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickDelete(v,getAdapterPosition());
                }
            });
            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickUpdate(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void onClickDelete(View v, int position);
        void onClickUpdate(View v, int position);
    }
}
