package com.example.warantee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.example.warantee.AddWaranteeForm.REQUEST_IMAGE_CAPTURE;

public class AddWaranteeForm2 extends AppCompatActivity {


    //Back Button
    private Button back;


    ImageView warrantyImage;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_warentee_form_2);

        //Button and  ImageView for the Camera
        Button captureImage = (Button) findViewById(R.id.captureImage);
        warrantyImage = (ImageView) findViewById(R.id.warrantyImage);

        //Check if the phone has a camera
        if (!hasCamera())
            captureImage.setEnabled(false);


        //Next Page Button
        back = (Button)  findViewById(R.id.BackButton) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddWaranteeForm();
            }
        });

        //Create submission button
        submit = (Button) findViewById(R.id.FormSubmitButton);

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
            Bundle extras = data.getExtras();
            Bitmap Photo = (Bitmap) extras.get("data");
            warrantyImage.setImageBitmap(Photo);
        }
    }
    //----------------------------- End Of Camera -----------------------------

    //----------------------------- OpenAddWarantee Function -----------------------
    public void openAddWaranteeForm(){
        Intent intent = new Intent(this, AddWaranteeForm.class);
        startActivity(intent);
    }
    //----------------------------- End of OpenAddWarantee -------------------------
}
