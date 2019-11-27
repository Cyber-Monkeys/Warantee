package com.example.warantee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.warantee.AddWaranteeForm.REQUEST_IMAGE_CAPTURE;

public class AddWaranteeForm2 extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TAKE_VIDEO = 2;
    ImageView warrantyImage;
    VideoView warantyVideo;
    MediaController media;
    Button submit;
    private String name;
    private String phone;
    private String email;
    private String date;
    private String WarantyPeriod;
    private int category;
    private String amount;
    private String location;
    private String currentPhotoPath, currentVideoPath;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        date = intent.getStringExtra("date");
        WarantyPeriod = intent.getStringExtra("period");
        category = intent.getIntExtra("category", 0);
        amount = intent.getStringExtra("amount");
        location = intent.getStringExtra("location");
        context = this.getApplicationContext();

        intent.putExtra("category", category);
        setContentView(R.layout.content_add_warentee_form_2);

        //Button and  ImageView for the Camera
        Button captureImage = (Button) findViewById(R.id.captureImage);
        warrantyImage = (ImageView) findViewById(R.id.warrantyImage);
        warantyVideo = (VideoView) findViewById(R.id.warantyVideo);
        //Check if the phone has a camera
        if (!hasCamera())
            captureImage.setEnabled(false);



        //Create submission button
        submit = (Button) findViewById(R.id.FormSubmitButton);

        media = new MediaController(this);

    }


    //----------------------------- Camera Functions -----------------------------
    //has a camera function
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY); // to check if the phone has a camera,
        // if not it will disable the button
    }

    //Launching the Camera
    public void launchCamera(View view) {
        //This intent is to to lunch the camera when this function is called
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //after taking the image we want to save so we start an activity to save the image
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    //Receiving the Image and displaying it in ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap Photo = (Bitmap) BitmapFactory.decodeFile(currentPhotoPath);
            warrantyImage.setImageBitmap(Photo);
        } else  if(requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            warantyVideo.setVideoPath(currentVideoPath);
            warantyVideo.setMediaController(media);
            media.setAnchorView(warantyVideo);
            warantyVideo.start();
        }
    }

    public void getPhotoFromCamera(View V) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createFile("photo_", ".jpg");
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
    public void getVideoFromCamera(View V) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                videoFile = createFile("video_", ".mp4");
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        videoFile);
                currentVideoPath = videoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_VIDEO);
            }
        }
    }
    public void submitWaranty(View V) {

        String stringUrl = "https://www.vrpacman.com/waranty";
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
                                new InsertWarantyTask(context).execute(stringUrl, idToken, date, amount + "", category + "", WarantyPeriod + "", name, phone, email, location, currentPhotoPath, currentVideoPath);

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
    private File createFile(String fileType, String ext) throws IOException {
        // Create an Video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = fileType + timeStamp + ext;//change code back
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = new File(storageDir, videoFileName);
//        File video = File.createTempFile(
//                videoFileName,  /* prefix */
//                ".mp4",         /* suffix */
//                storageDir      /* directory */
//        );

        // Save a file: path for use with ACTION_VIEW intents
        currentVideoPath = video.getAbsolutePath();
        return video;
    }
    //----------------------------- End Of Camera -----------------------------


    //----------------------------- End of OpenAddWarantee -------------------------

}
