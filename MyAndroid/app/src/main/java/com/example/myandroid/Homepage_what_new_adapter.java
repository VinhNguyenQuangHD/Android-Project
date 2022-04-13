package com.example.myandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Homepage_what_new_adapter extends RecyclerView.Adapter<Homepage_what_new_adapter.MyViewHolder> {

    private RecyclerviewClick click;
    private ArrayList<Book_overal> list;

    public Homepage_what_new_adapter(ArrayList<Book_overal> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public Homepage_what_new_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_homepage_what_new_list,parent,false);

        return new Homepage_what_new_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Homepage_what_new_adapter.MyViewHolder holder, int position) {
       String[] background_color = {"#FFE6E6","#F9FFCD","#BCCAFD","#BDFAD5","#F1EDEE"};
        holder.layout.setBackgroundColor(Color.parseColor(background_color[position]));
        holder.book_author.setText(list.get(position).getBook_author());
        holder.book_name.setText(list.get(position).getBook_name());
        holder.book_watch.setText(list.get(position).getBook_point());
        Glide.with(holder.img.getContext()).load(list.get(position).getBook_img()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView book_name,book_author,book_watch;
        RelativeLayout layout;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.homepage_book_name_what_new);
            book_author = itemView.findViewById(R.id.homepage_book_author_what_new);
            book_watch = itemView.findViewById(R.id.homepage_book_price_what_new);
            layout = itemView.findViewById(R.id.homepage_whatnew_frame);
            img = itemView.findViewById(R.id.homepage_book_img_what_new);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            click.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerviewClick{
        void onClick(View v, int position);
    }
}
