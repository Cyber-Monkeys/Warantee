package com.example.warantee;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;


public class AddWaranteeForm extends AppCompatActivity {

    //Next Button
    private Button next;


    //Variables for capturing and Viewing the Image
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView warrantyImage;


    //Variables for Picking the date
    EditText myDate;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog dpd ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_warantee_form);

        //Next Page Button
        next = (Button)  findViewById(R.id.nextPageButton) ;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddWaranteeForm2();
            }
        });



        //Date Picker Code
        myDate = (EditText) findViewById(R.id.editText3);
        myDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                int month = myCalendar.get(Calendar.MONTH);
                int year = myCalendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(AddWaranteeForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        myDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, day, month, year);
                    dpd.show();
            }
        });


//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateDate();
//            }
//        };

        Button submitButton;

        EditText editText1;
        EditText editText2;
        EditText editText4;
        EditText editText5;
        EditText editText6;
        EditText editText7;


    }

    //----------------------------- OpenAddWarantee Function -----------------------
    public void openAddWaranteeForm2(){
        Intent intent = new Intent(this, AddWaranteeForm2.class);
        startActivity(intent);
    }
    //----------------------------- End of OpenAddWarantee -------------------------


    //----------------------------- DatePicker Function -----------------------
//    private void updateDate() {
//        String myFormat = "DD/mm/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
//
//        myDate.setText(sdf.format(myCalendar.getTime()));
//    }
    //----------------------------- End Of DatePicker --------------------------
}
