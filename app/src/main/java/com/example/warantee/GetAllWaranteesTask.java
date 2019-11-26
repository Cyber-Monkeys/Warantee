package com.example.warantee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;

public class GetAllWaranteesTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... urls) {
        try {
            downloadFile(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String downloadFile(String token) throws IOException, GeneralSecurityException {
        String myurl = "http://192.168.43.232:3000/waranty";
        InputStream is = null;
        OutputStream outStream = null;
        File targetFile = null;
        String inputLine;

        String result = "";
        Log.d("result1startdownload", myurl);
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Log.d("res1", token);
            conn.setRequestProperty("AuthToken", token);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.connect();

            // Starts the query
            int response = conn.getResponseCode();
            Log.d("res1", response + "");
            InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            Log.d("result1", stringBuilder.toString());
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
            //result = processJSON(result);
            return result;
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
        return targetFile.getAbsolutePath();

    }
    private String processJSON(String result){
        JSONObject jsonObject = null;
        int warantyId = -1;
        try {
            jsonObject = new JSONObject(result);
            warantyId = jsonObject.getInt("id");
            String uid = jsonObject.getString("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return warantyId + "";
    }

}
