package com.example.warantee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class waranteeList extends AppCompatActivity {
    GoogleSignInClient googleSignInClient;
    private Toolbar toolbar;

    private ListView listView;
    private WarantyAdapter mAdapter;
    private TextView tvEmptyView;
    private Context context;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    private ArrayList<Waranty> warantyList;
    private int lengthOfWarantees = 0;

    protected Handler handler;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_warantee_list);
        listView = (ListView) findViewById(R.id.warantees);
        warantyList = new ArrayList<Waranty>();
        mydatabase = openOrCreateDatabase("WaranteeDatabase",MODE_PRIVATE,null);

        mydatabase.beginTransaction();
        try {
            //perform your database operations here ...
            // delete any existing table
            // create a new table for restaurants
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Waranty(id INTEGER PRIMARY KEY,uid VARCHAR, date VARCHAR, amount FLOAT, category INTEGER, warantyPeriod INTEGER, sellerName VARCHAR, sellerPhone VARCHAR, sellerEmail VARCHAR );");
            mydatabase.setTransactionSuccessful(); //commit your changes
        }
        catch (Exception e) {
            //report problem
            Log.d("res1", "error in creating table");
        }
        finally {
            mydatabase.endTransaction();
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                warantyList.get(bundle.getInt("warantyIndex")).setImageLocation(bundle.getString("warantyLocation"));
                Log.d("res1", warantyList.get(bundle.getInt("warantyIndex")).toString());
                if(bundle.getInt("warantyIndex") == warantyList.size() - 1) {
                    mAdapter = new WarantyAdapter(context, warantyList); listView.setAdapter(mAdapter);
                    //listView.setAdapter(mAdapter);
                }
                //update local database here?
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent i = new Intent(parent.getContext(),WarrantyInfo.class);
                i.putExtra("id", warantyList.get(position).getId());
                startActivity(i);
            }

        });
                mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(waranteeList.this, Login.class));
                }
            }
        };


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Android Students");

        }
        ArrayList<Waranty> subjectsList = new ArrayList<>();
        subjectsList.add(new Waranty());
        mAdapter = new WarantyAdapter(this, subjectsList); listView.setAdapter(mAdapter);
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
                                new GetAllWaranteesTask().execute(idToken);
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
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void LogOutButtonPressed(View V) {
        mFirebaseAuth.signOut();
        mGoogleSignInClient.signOut();
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
        mydatabase.beginTransaction();
        try {
            //perform your database operations here ...
            // delete any existing table
            mydatabase.execSQL("DROP TABLE IF EXISTS Restaurant");
            // create a new table for restaurants
            mydatabase.setTransactionSuccessful(); //commit your changes
        }
        catch (Exception e) {
            //report problem
            Log.d("res1", "error in dropping table");
        }
        finally {
            mydatabase.endTransaction();
        }

    }

    public void addWarantyPressed(View V) {
        startActivity(new Intent(waranteeList.this, AddWaranteeForm.class));
    }

    public class GetAllWaranteesTask extends AsyncTask<String, Void, Void> {
        String idtoken;
        @Override
        protected Void doInBackground(String... urls) {
            this.idtoken = urls[0];
            try {
                downloadFile(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            Log.d("res1", "start PostExecute" + warantyList.size());
            for(int i = 0;i < lengthOfWarantees;i++) {
                Thread obj = new Thread(new GetWaranteeImageTask("https://www.vrpacman.com/s3proxy?fileKey=" + warantyList.get(i).getUid() + warantyList.get(i).getId() + ".jpg", i, warantyList.get(i).getId(), getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), this.idtoken, handler ));
                obj.start();
            }
        }
        private String downloadFile(String token) throws IOException, GeneralSecurityException {
            String myurl = "https://www.vrpacman.com/waranty";
            InputStream is = null;
            OutputStream outStream = null;
            File targetFile = null;
            String inputLine;

            String result = "";
            Log.d("result1startdownload", myurl);
            try {
                URL url = new URL(myurl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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
                processJSON(result);
                Log.d("res2", result);
                return stringBuilder.toString();
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
            return "";

        }
        private void processJSON(String result){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                for(int i = 0;i < jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String warantyId = jsonObject.getString("id");
                    String uid = jsonObject.getString("uid");
                    String date = jsonObject.getString("date");
                    float amount = (float) jsonObject.getDouble("amount");
                    String category = jsonObject.getInt("category") + "";
                    int warantyPeriod = jsonObject.getInt("warantyPeriod");
                    String sellerName = jsonObject.getString("sellerName");
                    String sellerPhone = jsonObject.getString("sellerPhone");
                    String sellerEmail = jsonObject.getString("sellerEmail");
                    warantyList.add(new Waranty(uid, warantyId, date, amount, category, warantyPeriod, sellerName, sellerPhone, sellerEmail));
                    mydatabase.beginTransaction();
                    try {
                        // insert downloaded data into database
                        ContentValues values = new ContentValues( );
                        values.put("id" , warantyId);
                        values.put("id" , uid);
                        values.put("date", date);
                        values.put("amount", amount);
                        values.put("category" , category);
                        values.put("warantyPeriod" , warantyPeriod);
                        values.put("sellerName", sellerName);
                        values.put("sellerPhone", sellerPhone);
                        values.put("sellerEmail", sellerEmail);
                        mydatabase.insert("Waranty" , "" , values);
                        mydatabase.setTransactionSuccessful(); //commit your changes
                    }
                    catch (Exception e) {
                        //report problem
                    }
                    finally {
                        mydatabase.endTransaction();
                    }
                }
                lengthOfWarantees = jsonArray.length();
                Log.d("jsondone", lengthOfWarantees + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
