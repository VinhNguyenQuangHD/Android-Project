package com.example.myandroid;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Library_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Library_frag extends Fragment {

    private ArrayList<Library> personal_libs;

    private ArrayList<Book_overal> recommends_list;

    private RecyclerView recycleview,recycleview2;

    private RelativeLayout add_new_lib;

    private Button fav_lib;

    private Librarypage_personal_lib_adapter.RecyclerviewClick listenner;

    String get_user;


    public Library_frag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Library_frag newInstance(String param1, String param2) {
        Library_frag fragment = new Library_frag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library_frag, container, false);
        //RecyclerView
        recycleview = view.findViewById(R.id.recycle_lib_personal);

        //Relative layout
        add_new_lib = view.findViewById(R.id.libpage_add_new_library);
        fav_lib = view.findViewById(R.id.libpage_favorite_librarys);

        Bundle getInstance = getActivity().getIntent().getExtras();
        get_user = getInstance.getString("email");

        personal_libs = new ArrayList<>();
        recommends_list= new ArrayList<>();

        //Them mot thu vien moi
        add_new_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewLibrary(Gravity.CENTER);
            }
        });

        //Mo thu vien yeu thich
        fav_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),LibraryDetail.class);
                i.putExtra("email",get_user);
                i.putExtra("lib_name","Thư viện yêu thích");
                i.putExtra("lib_description","Chỉ hiển thị những đầu sách được thêm vào thư viện yêu thích");
                i.putExtra("lib_type","1");
                startActivity(i);
            }
        });

        setAdapter();

        // Inflate the layout for this fragment
        return view;
    }

    private void AddNewLibrary(int gravity) {
        final Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_librarypage_create_new_library);

        Window window = custom_dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win_attr = window.getAttributes();
        win_attr.gravity = gravity;
        window.setAttributes(win_attr);

        if(Gravity.BOTTOM == gravity){
            custom_dialog.setCancelable(true);
        }else{
            custom_dialog.setCancelable(false);
        }

        Button cancel = custom_dialog.findViewById(R.id.add_new_lib_cancel);
        Button add_confirm = custom_dialog.findViewById(R.id.add_new_lib_confirm);
        EditText lib_name = custom_dialog.findViewById(R.id.add_new_lib_lib_name_text);
        EditText lib_description = custom_dialog.findViewById(R.id.add_new_lib_lib_description_text);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference lib_ref = FirebaseDatabase.getInstance().getReference("thuvien");
                Library lib = new Library(get_user,lib_name.getText().toString(),lib_description.getText().toString());
                lib_ref.child(lib_name.getText().toString()).setValue(lib);

                Toast.makeText(getContext(), "Thư viện đã được tạo", Toast.LENGTH_SHORT).show();
                custom_dialog.dismiss();
            }
        });
        custom_dialog.show();
    }

    private void setAdapter() {
        personal_libs = new ArrayList<>();
        OpenLibs();
        Librarypage_personal_lib_adapter adapter = new Librarypage_personal_lib_adapter(personal_libs,listenner);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        DatabaseReference per_lib_ref = FirebaseDatabase.getInstance().getReference("thuvien");
        per_lib_ref.orderByChild("email").equalTo(get_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personal_libs.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Library lib = dataSnapshot.getValue(Library.class);
                    personal_libs.add(lib);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recycleview.setLayoutManager(manager);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setAdapter(adapter);
    }

    private void OpenLibs() {
        listenner = new Librarypage_personal_lib_adapter.RecyclerviewClick() {
            @Override
            public void onClickDelete(View v, int position) {
                String key = personal_libs.get(position).getLib_name().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("thuvien").orderByChild("lib_name").equalTo(key);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void OpenLib(View v, int position) {
                Intent i = new Intent(getContext(),LibraryDetail.class);
                i.putExtra("email",personal_libs.get(position).getEmail());
                i.putExtra("lib_name",personal_libs.get(position).getLib_name());
                i.putExtra("lib_description",personal_libs.get(position).getLib_description());
                i.putExtra("lib_type","0");
                startActivity(i);
            }
        };
    }
}