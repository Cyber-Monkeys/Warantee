package com.example.warantee;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DecrementWarantyTask extends AsyncTask<String,Void,Void>{

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
