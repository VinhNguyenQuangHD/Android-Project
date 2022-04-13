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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountInformationPage_all_account_adapter extends RecyclerView.Adapter<AccountInformationPage_all_account_adapter.MyViewHolder> {

    ArrayList<Account_infor> acc_list;
    RecyclerviewClick click;

    public AccountInformationPage_all_account_adapter(ArrayList<Account_infor> acc_list, RecyclerviewClick click) {
        this.acc_list = acc_list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_accountinformationpage_all_account,parent,false);
        return new AccountInformationPage_all_account_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.card_all.setBackgroundColor(Color.TRANSPARENT);
        holder.username_name.setTextColor(Color.parseColor("#ffffff"));

        String text_user = acc_list.get(position).getUsername();
        String new_text = "";
        if(text_user.length() >12){
            new_text = text_user.substring(0,12)+"...".toString();
            holder.username_name.setText(new_text);
        }
        else{
            holder.username_name.setText(text_user);
        }

        Glide.with(holder.img.getContext()).load(acc_list.get(position).getImgsrc())
                .apply(RequestOptions.circleCropTransform()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return acc_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username_name;
        private CardView card_all;
        private RelativeLayout layout;
        private ImageButton send_friend_request;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username_name = itemView.findViewById(R.id.account_name_load);
            img = itemView.findViewById(R.id.account_image_load);
            card_all = itemView.findViewById(R.id.card_all_account);
            layout = itemView.findViewById(R.id.all_account_frame);
            send_friend_request = itemView.findViewById(R.id.add_friend_btn);

            send_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.SendingRequest(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void SendingRequest(View v, int position);
    }
}
