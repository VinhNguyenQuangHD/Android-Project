package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Notificationpage_gift_receiver_adapter extends RecyclerView.Adapter<Notificationpage_gift_receiver_adapter.MyViewHolder> {
    ArrayList<GiftReceiver> list;
    RecyclerviewClick click;

    public Notificationpage_gift_receiver_adapter(ArrayList<GiftReceiver> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notificationpage_gift_receiver,parent,false);
        return new Notificationpage_gift_receiver_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getFrom());
        holder.content.setText("Bạn đã được tặng một đầu sách:"+list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,content;
        private ImageButton add_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.gift_receiver_from_name_txt);
            content = itemView.findViewById(R.id.gift_receiver_content_txt);
            add_btn = itemView.findViewById(R.id.gift_receiver_add_btn);

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClickAdd(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void onClickAdd(View v, int position);
    }
}
