package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Notificationpage_normal_notification_adapter extends RecyclerView.Adapter<Notificationpage_normal_notification_adapter.MyViewHolder> {
    ArrayList<Notification> list;
    RecyclerviewClick click;

    public Notificationpage_normal_notification_adapter(ArrayList<Notification> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public Notificationpage_normal_notification_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notificationpage_normal_notification,parent,false);

        return new Notificationpage_normal_notification_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Notificationpage_normal_notification_adapter.MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getFrom());
        holder.content.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,content;
        private ImageButton delete_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.normal_from_name_txt);
            content = itemView.findViewById(R.id.normal_content_txt);
            delete_btn = itemView.findViewById(R.id.notifi_delete_btn);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClickDelete(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void onClickDelete(View v, int position);
    }
}
