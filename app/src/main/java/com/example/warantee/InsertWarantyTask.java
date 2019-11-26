package com.example.warantee;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class InsertWarantyTask extends AsyncTask<String, Void, String> {

    public String currentPhotoPath;
    public String currentVideoPath;
    public static String idToken;

    Context context;
    InsertWarantyTask(Context context) {
        this.context = context.getApplicationContext();
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d("res1", params[9]);
            this.currentPhotoPath = params[9];
            this.currentVideoPath = params[10];
            InsertWarantyTask.idToken = params[1];
            return  downloadUrl(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new UploadPhotoTask(context).execute(InsertWarantyTask.idToken,result, this.currentPhotoPath, this.currentVideoPath);
    }

    private String downloadUrl(String myurl, String idToken, String date, String amount, String category, String warantyPeriod, String sellerName, String sellerPhone, String sellerEmail ) throws IOException {
        InputStream is = null;
        Log.d("res1", amount);
        float parseAmount = Float.parseFloat(amount);
        int parseWarantyPeriod = Integer.parseInt(warantyPeriod);
        int parseCategory = Integer.parseInt(category);
        String inputLine;
        DataOutputStream os = null;
        // Only display the first 500 characters of the retrieved web page content.
        int len = 500;

        String result = "";
        try {
            URL url = new URL(myurl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthToken", idToken);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("date", date);
            jsonParam.put("amount", parseAmount);
            jsonParam.put("category", parseCategory);
            jsonParam.put("warantyPeriod", parseWarantyPeriod);
            jsonParam.put("sellerName", sellerName);
            jsonParam.put("sellerPhone", sellerPhone);
            jsonParam.put("sellerEmail", sellerEmail);
            os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());
            conn.connect();
            int response = conn.getResponseCode();
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
            result = processJSON(result);
            return result;
        } catch(Exception e) {
            Log.d("error", e.getMessage());
        }finally {


            if (is != null) {
                is.close();
            }
            if(os != null) {
                os.flush();
                os.close();
            }
            return result;
        }
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

