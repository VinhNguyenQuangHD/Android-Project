package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationPage_friend_request_adapter extends RecyclerView.Adapter<NotificationPage_friend_request_adapter.MyViewHolder>{

    ArrayList<Notification> list;
    RecyclerviewClick click;

    public NotificationPage_friend_request_adapter(ArrayList<Notification> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notificationpage_friend_request_list,parent,false);

        return new NotificationPage_friend_request_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getFrom());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        private ImageButton accept_btn,reject_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.friend_from_txt);
            accept_btn = itemView.findViewById(R.id.friend_accept_btn);
            reject_btn = itemView.findViewById(R.id.friend_refuse_btn);

            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClickAccept(v,getAdapterPosition());
                }
            });

            reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClickReject(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void onClickAccept(View v, int position);
        void onClickReject(View v, int position);
    }

}
