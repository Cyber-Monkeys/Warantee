package com.example.warantee;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddWaranteeForm extends AppCompatActivity {

    //Variables for capturing and Viewing the Image
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView warrantyImage;

    //Variables for Picking the date
    EditText myDate;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_warantee_form);

        //Date Thingy xd
        myDate = (EditText) findViewById(R.id.editText3);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };


        //Button and  ImageView for the Camera
        Button captureImage = (Button) findViewById(R.id.captureImage);
        warrantyImage = (ImageView) findViewById(R.id.warrantyImage);

        //Check if the phone has a camera
        if (!hasCamera())
            captureImage.setEnabled(false);



        Button submitButton;

        EditText editText1;
        EditText editText2;
        EditText editText4;
        EditText editText5;
        EditText editText6;
        EditText editText7;


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

    //----------------------------- DatePicker Function -----------------------
    private void updateDate() {
        String myFormat = "DD/mm/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        myDate.setText(sdf.format(myCalendar.getTime()));
    }
    //----------------------------- End Of DatePicker --------------------------
}
