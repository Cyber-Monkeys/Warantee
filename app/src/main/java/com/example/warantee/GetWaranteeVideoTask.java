package com.example.warantee;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

// gets image from server and stores it in internal storage
public class GetWaranteeVideoTask implements Runnable {
    String url;
    String warantyId;
    String token;
    String directory;
    Handler handler;

    public GetWaranteeVideoTask(String url, String warantyId, String directory, String token, Handler handler) {
        this.url = url;
        this.token = token;
        this.handler = handler;
        this.warantyId = warantyId;
        this.directory = directory;
    }
    @Override
    public void run() {
        Message msg = this.handler.obtainMessage();
        InputStream is = null;
        OutputStream outStream = null;
        File targetFile = null;
        String inputLine;

        String result = "";
        Log.d("result1startdownload", this.url);
        try {
            URL url = new URL(this.url);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Log.d("res1", this.token);
            conn.setRequestProperty("AuthToken", this.token);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.connect();

            // Starts the query
            int response = conn.getResponseCode();
            Log.d("res1", response + "");
            is = conn.getInputStream();
            targetFile = new File(this.directory + warantyId + ".mp4");
            outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[8 * 200 * 1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch(Exception e) {
            Log.d("error", e.getMessage());
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        handler.sendMessage(msg);
    }
}
