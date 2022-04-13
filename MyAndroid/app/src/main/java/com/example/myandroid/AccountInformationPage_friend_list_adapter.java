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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountInformationPage_friend_list_adapter extends RecyclerView.Adapter<AccountInformationPage_friend_list_adapter.MyViewHolder> {
    ArrayList<Friend_req> list;
    RecyclerviewClick click;

    public AccountInformationPage_friend_list_adapter(ArrayList<Friend_req> acc_list, RecyclerviewClick click) {
        this.list = acc_list;
        this.click = click;
    }

    @NonNull
    @Override
    public AccountInformationPage_friend_list_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_accountinformationpage_friend_list,parent,false);
        return new AccountInformationPage_friend_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountInformationPage_friend_list_adapter.MyViewHolder holder, int position) {

        holder.card_all.setBackgroundColor(Color.TRANSPARENT);
        holder.username_name.setTextColor(Color.parseColor("#ffffff"));

        //Load ten dai dien va hinh
        DatabaseReference acc_infor_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        acc_infor_ref.orderByChild("email").equalTo(list.get(position).getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernames = "",img_links="";
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    usernames = infor.getUsername();
                    img_links = infor.getImgsrc();
                }

                holder.username_name.setText(usernames);
                Glide.with(holder.img).load(img_links).apply(RequestOptions.circleCropTransform()).into(holder.img);
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
        private TextView username_name;
        private CardView card_all;
        private RelativeLayout layout;
        private ImageButton delete_friend_request;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username_name = itemView.findViewById(R.id.friend_list_name_load);
            img = itemView.findViewById(R.id.friend_list_image_load);
            card_all = itemView.findViewById(R.id.card_friend_list);
            layout = itemView.findViewById(R.id.friend_list_frame);
            delete_friend_request = itemView.findViewById(R.id.friend_list_delete_friend_btn);

            delete_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.DeleteRequest(v,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerviewClick{
        void DeleteRequest(View v, int position);
    }
}
