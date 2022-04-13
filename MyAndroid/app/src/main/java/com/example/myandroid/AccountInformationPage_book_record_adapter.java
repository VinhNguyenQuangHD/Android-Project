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

public class AccountInformationPage_book_record_adapter extends RecyclerView.Adapter<AccountInformationPage_book_record_adapter.MyViewHolder> {

    ArrayList<Book_history> list;

    public AccountInformationPage_book_record_adapter(ArrayList<Book_history> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_account_information_book_record,parent,false);
        return new AccountInformationPage_book_record_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getTime());
        holder.text2.setText(list.get(position).getBook_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,text2;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.book_record_time);
            text2 = itemView.findViewById(R.id.book_record_book_name);
        }
    }
}
