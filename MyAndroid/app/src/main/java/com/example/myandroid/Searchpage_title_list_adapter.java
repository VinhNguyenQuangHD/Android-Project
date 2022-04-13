package com.example.myandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Searchpage_title_list_adapter extends RecyclerView.Adapter<Searchpage_title_list_adapter.MyViewHolder>{

    private ArrayList<Searchingpage_title_list> title_lists;

    public Searchpage_title_list_adapter(ArrayList<Searchingpage_title_list> title_lists) {
        this.title_lists = title_lists;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,text2;
        private ImageView img,img2;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.title_text_txt);
            text2 = itemView.findViewById(R.id.title_text2_txt);
            img = itemView.findViewById(R.id.image_title);
            img2 = itemView.findViewById(R.id.image_title_2);
        }
    }

    @NonNull
    @Override
    public Searchpage_title_list_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_searching_title_list,parent,false);
        return new Searchpage_title_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Searchpage_title_list_adapter.MyViewHolder holder, int position) {
        String text1 = title_lists.get(position).getTen_chu_de(),
                text2 = title_lists.get(position).getTen_chu_de2();
        holder.text.setText(text1);
        holder.text2.setText(text2);
        int img = title_lists.get(position).getAnh_chu_de(),
                img2 = title_lists.get(position).getAnh_chu_de2();
        holder.img.setImageResource(img);
        holder.img2.setImageResource(img2);
    }

    @Override
    public int getItemCount() {
        return title_lists.size();
    }
}
