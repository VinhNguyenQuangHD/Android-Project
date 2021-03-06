package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Adminpage_AccountManager extends AppCompatActivity {

    RecyclerView account_view;
    Adminpage_accountmanager_list_adapter.RecyclerviewClick listen;

    Bundle bundle;
    ArrayList<Account_infor> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_account_manager);
        account_view = findViewById(R.id.admin_account_manager_list);

        bundle = getIntent().getExtras();

        list = new ArrayList<>();

        AccountEvent();
        Adminpage_accountmanager_list_adapter adapter = new Adminpage_accountmanager_list_adapter(list,listen);
        DatabaseReference acc_list_ref = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
        acc_list_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Account_infor infor = dataSnapshot.getValue(Account_infor.class);
                    list.add(infor);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        account_view.setLayoutManager(new LinearLayoutManager(this));
        account_view.setItemAnimator(new DefaultItemAnimator());
        account_view.setAdapter(adapter);

    }

    private void AccountEvent() {
        listen = new Adminpage_accountmanager_list_adapter.RecyclerviewClick() {
            @Override
            public void onClickWarning(View v, int position) {
                DatabaseReference notification = FirebaseDatabase.getInstance().getReference("thongbao");
                Notification warning_sending = new Notification(bundle.getString("email"),list.get(position).getEmail()
                        ,"T??i kho???n c???a b???n ???? b??? m???t c???nh b??o t??? ph??a qu???n tr??? vi??n","2");
                String email = bundle.getString("email");
                notification.child(email.replace(".","")).setValue(warning_sending);

                Toast.makeText(Adminpage_AccountManager.this, "???? g???i c???nh b??o ?????n ng?????i d??ng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickDisable(View v, int position) {
                HashMap change_type = new HashMap();
                change_type.put("acc_type","disable");

                String email = list.get(position).getEmail();

                DatabaseReference disable = FirebaseDatabase.getInstance().getReference("thongtintaikhoan");
                disable.child(email.replace(".","")).updateChildren(change_type).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Adminpage_AccountManager.this, "???? v?? hi???u h??a ng?????i d??ng", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Adminpage_AccountManager.this, "C?? l???i x???y ra", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };
    }
}