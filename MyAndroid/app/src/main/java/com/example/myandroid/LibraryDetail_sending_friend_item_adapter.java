package com.example.myandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LibraryDetail_sending_friend_item_adapter extends RecyclerView.Adapter<LibraryDetail_sending_friend_item_adapter.MyViewHolder> {
    ArrayList<Friend_req> list;
    RecyclerviewClick click;

    public LibraryDetail_sending_friend_item_adapter(ArrayList<Friend_req> acc_list, RecyclerviewClick click) {
        this.list = acc_list;
        this.click = click;
    }

    @NonNull
    @Override
    public LibraryDetail_sending_friend_item_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_sending_friend_item,parent,false);
        return new LibraryDetail_sending_friend_item_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryDetail_sending_friend_item_adapter.MyViewHolder holder, int position) {
        String text_user = list.get(position).getUsername();
        if(text_user.length() >12){
            holder.username_name.setText(text_user.substring(0,12)+"...");
        }
        else{
            holder.username_name.setText(text_user);
        }

        holder.card_all.setBackgroundColor(Color.TRANSPARENT);
        holder.layout.setBackgroundColor(Color.TRANSPARENT);
        holder.username_name.setTextColor(Color.parseColor("#ffffff"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username_name;
        private CardView card_all;
        private RelativeLayout layout;
        private ImageButton delete_friend_request;
        //private CircleImageView img;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username_name = itemView.findViewById(R.id.send_to_friend_name_load);
            //img = itemView.findViewById(R.id.account_name_load);
            card_all = itemView.findViewById(R.id.card_send_to_friend);
            layout = itemView.findViewById(R.id.send_to_friend_frame);
            delete_friend_request = itemView.findViewById(R.id.send_to_friend_in_list_btn);

            delete_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.SendingGift(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void SendingGift(View v, int position);
    }
}
