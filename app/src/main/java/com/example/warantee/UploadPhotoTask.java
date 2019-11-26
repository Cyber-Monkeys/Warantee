package com.example.warantee;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadPhotoTask extends AsyncTask<String, Void, String> {
    public String warantyId;
    public String currentVideoPath;
    public String idToken;
    @Override
    protected String doInBackground(String... urls) {
        this.currentVideoPath = urls[3];
        this.warantyId = urls[1];
        this.idToken = urls[0];
        return uploadPhoto(urls[0], urls[1], urls[2]);
    }

    @Override
    protected  void onPostExecute(String result) {
        Log.d("result1", result);
        new UploadVideoTask().execute(this.idToken, this.warantyId, this.currentVideoPath);
    }
    public String uploadPhoto(String idToken, String warantyId, String filePath) {
        String urlName = "http://172.28.24.229:3000/photo";
        String fileName = filePath;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(filePath);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
            return null;
        }

        int serverResponseCode = 0;
        try {
            Log.d("res3", "start sending");
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(urlName);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("AuthToken", idToken);
            conn.setRequestProperty("Connection", "Keep-Alive");
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
        if (serverResponseCode == 200) {
            return "done";
        }else {
            return "Could not upload";
        }
    }
}