package com.example.warantee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class WarrantyInfo extends AppCompatActivity {

    TextView sellerName, sellerPhone, sellerEmail, amount, date, warantyPeriod, category;
    ImageView imageView;
    VideoView videoView;
    SQLiteDatabase mydatabase;
    Handler handler;
    MediaController media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty_info);

        sellerName = findViewById(R.id.seller_name);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerEmail = findViewById(R.id.seller_email);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        warantyPeriod = findViewById(R.id.warranty_period);
        category = findViewById(R.id.category);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        media = new MediaController(this);



        mydatabase = openOrCreateDatabase("WaranteeDatabase",MODE_PRIVATE,null);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Cursor c = mydatabase.query("Waranty", null, "id=?", new String[]{id}, null, null, null);
        c.moveToNext();
        date.setText(c.getString(c.getColumnIndex("date")));
        amount.setText(c.getString(c.getColumnIndex("amount")));
        warantyPeriod.setText(c.getString(c.getColumnIndex("warantyPeriod")));
        category.setText(c.getString(c.getColumnIndex("category")));
        sellerName.setText(c.getString(c.getColumnIndex("sellerName")));
        sellerPhone.setText(c.getString(c.getColumnIndex("sellerPhone")));
        sellerEmail.setText(c.getString(c.getColumnIndex("sellerEmail")));
        Bitmap bm = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + id + ".jpg");
        imageView.setImageBitmap(bm);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("res3", "complete video download");
                videoView.setVideoPath(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + id + ".mp4");
                videoView.setMediaController(media);
                media.setAnchorView(videoView);
                videoView.start();
            }
        };
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                Log.d("res3", "start upload");
                                Thread obj = new Thread(new GetWaranteeVideoTask("https://www.vrpacman.com/s3proxy?fileKey=" + c.getString(c.getColumnIndex("uid")) + id + ".mp4", id, getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),idToken, handler));
                                obj.start();
                            } else {
                                Log.d("result2", "error");
                            }
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            Log.d("res3", "no token verified");
                        }
                    }
                });
        //download Video

    }
}
