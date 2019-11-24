package com.example.warantee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class waranteeList extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TAKE_VIDEO = 2;
    //OkHttpClient client;
    GoogleSignInClient googleSignInClient;
    private Toolbar toolbar;

    private ListView listView;
    private WarantyAdapter mAdapter;
    private TextView tvEmptyView;
    private ImageView stupidImageView;
    private VideoView stupidVideoView;
    private String encryptedFilePath;
    private Context context;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private List<Waranty> warantyList;


    protected Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_warantee_list);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        listView = (ListView) findViewById(R.id.warantees);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        stupidImageView = (ImageView) findViewById(R.id.stupid_image_view);
        stupidVideoView = (VideoView) findViewById(R.id.stupid_video_view);
        warantyList = new ArrayList<Waranty>();
        handler = new Handler();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Android Students");

        }
        ArrayList<Waranty> subjectsList = new ArrayList<>();
        subjectsList.add(new Waranty());
        mAdapter = new WarantyAdapter(this, subjectsList); listView.setAdapter(mAdapter);



        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("res3", getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        File mypath=new File(directory,"cool5848960802599173994.jpg");
        Bitmap bm = BitmapFactory.decodeFile(mypath.getAbsolutePath());
        stupidImageView.setImageBitmap(bm);

    }
    String currentPhotoPath, currentVideoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "cool";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private File createVideoFile() throws IOException {
        // Create an Video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "roman";//change code back
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentVideoPath = video.getAbsolutePath();
        return video;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("res3", "complete photo taken");
            Log.d("res3", currentPhotoPath);

            String encryptedFile = "";
            encryptedFile = currentPhotoPath;

            String stringUrl = "http://192.168.43.232:3000/photo";
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            String finalEncryptedFile = encryptedFile;
            new uploadPhoto().execute(stringUrl, finalEncryptedFile, "LOL");
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                                if (networkInfo != null && networkInfo.isConnected()) {
                                    Log.d("res3", "start upload");

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
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            File videoFile = new File(currentVideoPath);
            Uri videoUri = Uri.fromFile(videoFile);
            stupidVideoView.setVideoURI(videoUri);
            stupidVideoView.start();
        }
    }

    public void CheckIfAuthenticated(View V) {

        // Gets the URL from the UI's text field.
        String stringUrl = "http://192.168.43.232:3000/cool";
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                new getWaranteesTask().execute(stringUrl, idToken);
                            } else {
                                Log.d("result2", "error");
                            }
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

        getPhotoFromCamera();
        // getVideoFromCamera();


    }
    public void getPhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                currentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    public void getVideoFromCamera() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            }
        }
    }
    public class getWaranteesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("result1url", urls[0]);
                return downloadUrl(urls[0], urls[1]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected  void onPostExecute(String result) {
            Log.d("result1", result);
        }
//        public String get(String url) throws IOException {
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }
    }

    private String downloadUrl(String myurl, String idToken) throws IOException {
        InputStream is = null;
        String inputLine;
        // Only display the first 500 characters of the retrieved web page content.
        int len = 500;

        String result = "";
        Log.d("result1startdownload", myurl);
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("idToken", idToken);
            conn.connect();
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());
            //conn.connect();
            os.flush();
            os.close();

            // Starts the query
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
            processJSON(result);
            return result;
        } catch(Exception e) {
            Log.d("error", e.getMessage());
        }finally {


            if (is != null) {
                is.close();
            }
            return result;
        }
    }
    public class uploadPhoto extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return uploadVideo(urls[0], urls[1], urls[2]);
        }

        @Override
        protected  void onPostExecute(String result) {
            Log.d("result1", result);
        }
//        public String get(String url) throws IOException {
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }
    }
    public String uploadVideo(String urlName, String filePath ,String idToken) {

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
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
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
//                outputStream.write(buffer, 0, bytesRead);
//                bytesRead = fileInputStream.read(buffer, 0, 1024);

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
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException ioex) {
            }
            return "done";
        }else {
            return "Could not upload";
        }
    }
    public String formatContent(InputStream stream, int len) throws
            IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    private void processJSON(String result){
        mAdapter.clear();
        JSONArray jsonArray = null;
        String formattedString = new String();
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String id = c.getString("id");
                String uid = c.getString("uid");
                String date = c.getString("date");
                int amount = c.getInt("amount");
                String category = c.getString("category");
                int warantyPeriod = c.getInt("warantyPeriod");
                String sellerName = c.getString("sellerName");
                String sellerPhone = c.getString("sellerPhone");
                String sellerEmail = c.getString("sellerEmail");
                mAdapter.add(new Waranty(id, date, amount, category, warantyPeriod, sellerName, sellerPhone, sellerEmail));
            }
            listView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void LogOutButtonPressed(View V) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                new DownloadFile().execute("http://192.168.43.232:3000/download?fileKey=1574616814316.jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString(), idToken);
                            } else {
                                Log.d("result2", "error");
                            }
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

    }

    public class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadFile(urls[0], urls[1], urls[2]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            return null;
        }
        private String downloadFile(String myurl, String directory, String token) throws IOException, GeneralSecurityException {
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
                is = conn.getInputStream();
                targetFile = new File(directory + "damn.jpg");
                outStream = new FileOutputStream(targetFile);
                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
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

        @Override
        protected void onPostExecute(String receivedFile) {
            super.onPostExecute(receivedFile);
            Bitmap bm = BitmapFactory.decodeFile(receivedFile);
            stupidImageView.setImageBitmap(bm);
        }
    }

}
