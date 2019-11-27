package com.example.warantee;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        SQLiteDatabase mydatabase = context.openOrCreateDatabase("WaranteeDatabase", Context.MODE_PRIVATE,null);
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");
        // read the database, check the

        Cursor c = mydatabase.query("Waranty", new String[]{"warantyPeriod", "id"}, null, null, null, null, null);
        boolean startNotification = false;
        while(c.moveToNext()) {
            int warantyPeriod = Integer.parseInt(c.getString(c.getColumnIndex("warantyPeriod")));
            Log.d(TAG, warantyPeriod + "");
            warantyPeriod -=1;
            if(warantyPeriod < 30) {
                startNotification = true;
            }
            ContentValues values = new ContentValues();
            values.put("warantyPeriod" , warantyPeriod + "");
            mydatabase.update("Waranty" , values ,"id=?" , new String[ ] {c.getString(c.getColumnIndex("id"))} );
        }
        if(startNotification) {
            NotificationScheduler.showNotification(context, waranteeList.class,
                    "One of your warnatees is about to expire", "Please hurry");
        }
        //Trigger the notification
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                Log.d("res3", "start upload");
                                new DecrementWarantyTask().execute(idToken);
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

    }

    public class DecrementWarantyTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String...token) {
            try {
                updateDatabase(token[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void updateDatabase(String token) throws IOException {
            String myurl = "https://www.vrpacman.com/update";
            InputStream is = null;
            OutputStream outStream = null;
            String inputLine;

            String result = "";
            try {
                URL url = new URL(myurl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("AuthToken", token);
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.connect();
                Log.d("res1", "get request");
                // Starts the query
                int response = conn.getResponseCode();
                InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
                Log.d("res2", result);
            } catch(Exception e) {
                Log.d("error", e.getMessage());
            }finally {
                if (is != null) {
                    is.close();
                }
                if(outStream != null) {
                    outStream.close();
                }
            }
        }

    }
}
