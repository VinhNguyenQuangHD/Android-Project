package com.example.myandroid;

import android.graphics.Color;
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

import java.util.ArrayList;

public class BookDetailPage_load_all_lib_adapter extends RecyclerView.Adapter<BookDetailPage_load_all_lib_adapter.MyViewHolder> {

    ArrayList<Library> list;
    RecyclerviewClick click;

    public BookDetailPage_load_all_lib_adapter(ArrayList<Library> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bookdetail_list_lib_item,parent,false);
        return new BookDetailPage_load_all_lib_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.card_all.setBackgroundColor(Color.TRANSPARENT);
        holder.layout.setBackgroundColor(Color.TRANSPARENT);
        holder.username_name.setTextColor(Color.parseColor("#ffffff"));

        String text_user = list.get(position).getLib_name();
        String new_text = "";
        if(text_user.length() >12){
            new_text = text_user.substring(0,12)+"...".toString();
            holder.username_name.setText(new_text);
        }
        else{
            holder.username_name.setText(text_user);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username_name;
        private CardView card_all;
        private RelativeLayout layout;
        private ImageButton send_friend_request;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username_name = itemView.findViewById(R.id.bookdetail_lib_list_load);
            card_all = itemView.findViewById(R.id.card_bookdetailpage_load);
            layout = itemView.findViewById(R.id.bookdetail_lib_list_frame);
            send_friend_request = itemView.findViewById(R.id.bookdetail_lib_list_add_btn);

            send_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.AddToLib(v, getAdapterPosition());
                }
            });
        }
    }

        public interface RecyclerviewClick{
            void AddToLib(View v, int position);
        }
}
