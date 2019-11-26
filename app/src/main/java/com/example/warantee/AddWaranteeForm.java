package com.example.warantee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;


public class AddWaranteeForm extends AppCompatActivity {

    //Next Button
    private Button next;
    private String name;
    private String phone;
    private String email;
    private String date;
    private int WarantyPeriod;
    private int category;
    private String amount;

    //Variables for capturing and Viewing the Image
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView warrantyImage;


    //Variables for Picking the date
    EditText myDate;
    EditText nameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText warantyPeriodEditText;
    EditText amountEditText;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog dpd ;
    private boolean isUserInteracting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_warantee_form);
        Spinner dropdown = findViewById(R.id.spinner3);
        Context context = this.getApplicationContext();
//set the spinners adapter to the previously created one.
        nameEditText = (EditText) findViewById(R.id.warantyName);
        phoneEditText = (EditText) findViewById(R.id.warantyPhone);
        emailEditText = (EditText) findViewById(R.id.warantyEmail);
        warantyPeriodEditText = (EditText) findViewById(R.id.warantyPeriod);
        amountEditText = (EditText) findViewById(R.id.warantyAmount);
        myCalendar = Calendar.getInstance();
        String[] spinnerTitles;
        int[] spinnerImages;

        spinnerTitles = new String[]{"Food", "Grocery", "Travel", "Electronics", "Others"};
        spinnerImages = new int[]{R.drawable.ic_local_dining_24px
                , R.drawable.ic_local_grocery_store_24px
                , R.drawable.ic_directions_car_24px
                , R.drawable.ic_devices_other_24px
                , R.drawable.ic_emoji_objects_24px};
        CustomFormAdapter mCustomAdapter = new CustomFormAdapter(this, spinnerTitles, spinnerImages);
        dropdown.setAdapter(mCustomAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                if (isUserInteracting) {
                    Toast.makeText(context, spinnerTitles[i], Toast.LENGTH_SHORT).show();
                    category = i;
                }
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });
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
                        date = dayOfMonth + "/" + (month+1) + "/" + year;
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

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracting = true;
    }
    //----------------------------- OpenAddWarantee Function -----------------------
    public void openAddWaranteeForm2(){
        Intent intent = new Intent(this, AddWaranteeForm2.class);
        intent.putExtra("name", nameEditText.getText() + "");
        intent.putExtra("phone", phoneEditText.getText() + "");
        intent.putExtra("email", emailEditText.getText() + "");
        intent.putExtra("date", date);
        intent.putExtra("period", warantyPeriodEditText.getText() + "");
        intent.putExtra("category", category);
        intent.putExtra("amount", amountEditText.getText() + "");
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
