package com.example.warantee;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

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


    }
}
