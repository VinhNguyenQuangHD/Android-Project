package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adminpage_accountmanager_list_adapter extends RecyclerView.Adapter<Adminpage_accountmanager_list_adapter.MyViewHolder> {
    ArrayList<Account_infor> list;
    RecyclerviewClick listenner;

    public Adminpage_accountmanager_list_adapter(ArrayList<Account_infor> list, RecyclerviewClick listenner) {
        this.list = list;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adminpage_accountmanager_list,parent,false);
        return new Adminpage_accountmanager_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(list.get(position).getUsername());
        holder.email.setText(list.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username,email;

        private ImageButton warning_btn,disable_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.admin_account_manager_username);
            email = itemView.findViewById(R.id.admin_account_manager_email);
            warning_btn = itemView.findViewById(R.id.send_warn_btn);
            disable_btn = itemView.findViewById(R.id.disable_btn);

            warning_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickWarning(v,getAdapterPosition());
                }
            });
            disable_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickDisable(v,getAdapterPosition());
                }
            });
        }
    }
    public interface RecyclerviewClick{
        void onClickWarning(View v, int position);
        void onClickDisable(View v, int position);
    }
}

