package com.example.warantee;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

public class UploadVideoTask extends AsyncTask<String, Void, Void> {

    Context context;
    UploadVideoTask(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected Void doInBackground(String... urls) {
        uploadVideo(urls[0], urls[1], urls[2]);
        return null;
    }
    @Override
    protected void onPostExecute(Void V) {
        super.onPostExecute(V);
        Intent intent = new Intent(context, waranteeList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void uploadVideo(String idToken, String warantyId, String filePath) {
        String urlName = "https://www.vrpacman.com/video";
        String fileName = filePath;
        HttpsURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 10 * 1024;

        File sourceFile = new File(filePath);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
        }

        int serverResponseCode = 0;
        try {
            Log.d("res3", "start sending");
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(urlName);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("AuthToken", idToken);
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
            conn.setRequestProperty("WarantyId", warantyId);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();
            dos.close();

            serverResponseCode = conn.getResponseCode();

            fileInputStream.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
