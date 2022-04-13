package com.example.myandroid;

import android.graphics.Color;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookDetailPage_commend_adapter extends RecyclerView.Adapter<BookDetailPage_commend_adapter.MyViewHolder> {

    ArrayList<Comment_report> list;
    RecyclerviewClick click;

    public BookDetailPage_commend_adapter(ArrayList<Comment_report> list, RecyclerviewClick click) {
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bookdetailpage_comment,parent,false);
        return new BookDetailPage_commend_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.username.setText(list.get(position).getUsername());
        holder.username_comment.setText(list.get(position).getContent());
        DatabaseReference commend_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        commend_ref.orderByChild("email").equalTo(list.get(position).getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username_finding = "",img_loader = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    username_finding = infor.getUsername();
                    img_loader = infor.getImgsrc();
                }

                holder.username.setText(username_finding);
                Glide.with(holder.img.getContext().getApplicationContext()).load(img_loader).apply(RequestOptions.circleCropTransform()).into(holder.img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username,username_comment;
        private CardView card;
        private RelativeLayout layout;
        private ImageView img;
        private ImageButton add_to_friend,report_this_comment;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.username_display_name);
            username_comment = itemView.findViewById(R.id.username_comment_got);
            card = itemView.findViewById(R.id.card_comment);
            layout = itemView.findViewById(R.id.username_layout_commend);
            img = itemView.findViewById(R.id.username_img);
            add_to_friend = itemView.findViewById(R.id.comment_add_new_friend);
            report_this_comment = itemView.findViewById(R.id.report_this_commend);

            add_to_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.AddNewFriend(v,getAdapterPosition());
                }
            });

            report_this_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.ReportThisCommend(v,getAdapterPosition());
                }
            });
        }

    }

    public interface RecyclerviewClick{
        void AddNewFriend(View v, int position);
        void ReportThisCommend(View v, int position);
    }
}
