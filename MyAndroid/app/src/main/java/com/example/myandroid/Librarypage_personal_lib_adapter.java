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

public class Librarypage_personal_lib_adapter extends RecyclerView.Adapter<Librarypage_personal_lib_adapter.MyViewHolder>{

    private ArrayList<Library> personal_libs;
    RecyclerviewClick listenner;

    public Librarypage_personal_lib_adapter(ArrayList<Library> personal_libs, RecyclerviewClick listenner) {
        this.personal_libs = personal_libs;
        this.listenner = listenner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text,text2;
        private ImageButton delete_btn;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.lib_name);
            text2 = itemView.findViewById(R.id.lib_des);
            delete_btn = itemView.findViewById(R.id.delete_lib_btn);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.onClickDelete(v,getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenner.OpenLib(v,getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public Librarypage_personal_lib_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_librarypage_list_personal_library,parent,false);
        return new Librarypage_personal_lib_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Librarypage_personal_lib_adapter.MyViewHolder holder, int position) {
        holder.text.setText(personal_libs.get(position).getLib_name());
        holder.text2.setText(personal_libs.get(position).getLib_description());
    }

    @Override
    public int getItemCount() {
        return personal_libs.size();
    }

    public interface RecyclerviewClick{
        void onClickDelete(View v, int position);
        void OpenLib(View v,int position);
    }
}
