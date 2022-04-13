package com.example.myandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.SyncStateContract;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ChapterDetailPage extends AppCompatActivity {
    private TextView text,page_text, time_count;

    private PDFView text2;

    String book_name,book_src;

    private ImageButton translate_btn;


    private static final String TAG = "PDF_VIEW_TAG";
    String original_text;
    Translator Eng_trans;
    EditText result_text, move_to_page;
    ImageButton move_to_page_btn;


    RecyclerView chapter_load;

    ArrayList<Book_chapter> list;
    String z;
    Context context;

    public static final long MAX_BYTES = 50000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail_page);
        text = findViewById(R.id.chapter_text_2);
        text2 = findViewById(R.id.chapter_content);
        page_text = findViewById(R.id.chapterdetail_page_number);
        time_count = findViewById(R.id.trial_count);
        move_to_page = findViewById(R.id.move_to_page);
        move_to_page_btn = findViewById(R.id.move_to_page_btn);

        context = this;

        Bundle getExtra = getIntent().getExtras();
        {
            if(getExtra != null){
                book_name = getExtra.getString("book_name");
                book_src = getExtra.getString("src");
                text.setText(book_name);
            }
        }


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:YYYY hh:mm:ss");
        String times = simpleDateFormat.format(calendar.getTime());

        //Lay lich su doc sach
        Book_history record = new Book_history(getExtra.getString("email"),book_name,times);
        DatabaseReference record_ref = FirebaseDatabase.getInstance().getReference("lichsu");
        record_ref.push().setValue(record);

        move_to_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBook(Integer.parseInt(move_to_page.getText().toString()));
            }
        });

        LoadBook(0);

        translate_btn = findViewById(R.id.translate_text_btn);
        translate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTranslate(Gravity.CENTER);
            }
        });

        //Lay bo dem doc sach cho nhung nguoi chua mua
        DatabaseReference lib_ref = FirebaseDatabase.getInstance().getReference("chitietthuvien");
        lib_ref.orderByChild("email").equalTo(getExtra.getString("email")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String book = "";
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lib_detail lib = dataSnapshot.getValue(Lib_detail.class);
                    book = lib.getBook_name();
                }
                if(!book_name.equals(book)){
                    TimeCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void TimeCount() {
        time_count.setVisibility(View.VISIBLE);

        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished),TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                time_count.setText(sDuration);
            }

            @Override
            public void onFinish() {
                time_count.setVisibility(View.GONE);

                Toast.makeText(ChapterDetailPage.this, "Thời gian dùng thử đã kết thúc", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    //Mo trinh dich van ban
    private void OpenTranslate(int center) {
        final Dialog custom_dialog = new Dialog(this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.dialog_translate_text);

        Window window = custom_dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win_attr = window.getAttributes();
        win_attr.gravity = center;
        window.setAttributes(win_attr);

        if(Gravity.BOTTOM == center){
            custom_dialog.setCancelable(true);
        }else{
            custom_dialog.setCancelable(false);
        }

        Button translate_btn = custom_dialog.findViewById(R.id.start_translate_btn);
        EditText beginer_text = custom_dialog.findViewById(R.id.chapterdetail_translate_text);
        result_text = custom_dialog.findViewById(R.id.chapterdetail_translate_result_text);
        Button cancel_translate_btn = custom_dialog.findViewById(R.id.cancel_translate_btn);

        //Dong trinh dich van ban
        cancel_translate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });

        //Dich van ban
        translate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                original_text = beginer_text.getText().toString();
                PrepareToTranslate();
            }
        });

        custom_dialog.show();
    }

    //Chuan bi ngon ngu bien dich
    private void PrepareToTranslate() {
        TranslatorOptions trans_opt = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.VIETNAMESE).build();
        Eng_trans = Translation.getClient(trans_opt);
        //Tai ngon ngu can phien dich, neu nhu chua co
        Eng_trans.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                StartTranslate();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChapterDetailPage.this, "Không thể tải ngôn ngữ dịch", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Dich van ban da ghi
    private void StartTranslate() {
        Eng_trans.translate(original_text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                result_text.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChapterDetailPage.this, "Không thể dịch văn bản", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //Hien thi dau sach
    private void LoadBook(int page_num) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sach");
        databaseReference.child(book_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pdffile ="" + snapshot.child("book_src").getValue();

                LoadBookFromUrl(pdffile, page_num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Hien thi dau sach tu url
    private void LoadBookFromUrl(String pdffile, int page_num) {
        StorageReference st_ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdffile);
        st_ref.getBytes(MAX_BYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                text2.fromBytes(bytes).defaultPage(page_num).swipeHorizontal(true).onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        int current_page = (page +1);
                        page_text.setText("Trang:"+String.valueOf(current_page));

                    }
                }).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(ChapterDetailPage.this, ":)", Toast.LENGTH_SHORT).show();
                    }
                }).onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Toast.makeText(ChapterDetailPage.this, "Co loi xay ra", Toast.LENGTH_SHORT).show();
                    }
                }).load();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChapterDetailPage.this, "Khong the tai file", Toast.LENGTH_SHORT).show();
            }
        });
    }

}