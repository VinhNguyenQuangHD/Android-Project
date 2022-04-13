package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminPage_ReportManager_list_adapter extends RecyclerView.Adapter<AdminPage_ReportManager_list_adapter.MyViewHolder> {
    ArrayList<Comment_report> list;
    RecyclerviewClick listenner;

    public AdminPage_ReportManager_list_adapter(ArrayList<Comment_report> list, RecyclerviewClick listenner) {
        this.list = list;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adminpage_reportmanager_list,parent,false);
        return new AdminPage_ReportManager_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.email.setText(list.get(position).getUsername());
        holder.content.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView email,content;

        private ImageButton response_btn,delete_report_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            content = itemView.findViewById(R.id.admin_report_manager_content);
            email = itemView.findViewById(R.id.admin_report_manager_username);
            response_btn = itemView.findViewById(R.id.response_btn);
            delete_report_btn = itemView.findViewById(R.id.delete_report_btn);

            response_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickResponse(v,getAdapterPosition());
                }
            });
            delete_report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickDeleteReport(v,getAdapterPosition());
                }
            });
        }
    }
    public interface RecyclerviewClick{
        void onClickResponse(View v, int position);
        void onClickDeleteReport(View v, int position);
    }
}
