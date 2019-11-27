package com.example.warantee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

// first form page
public class AddWaranteeForm extends AppCompatActivity {

    //Next Button
    private Button next;
    private String name = "Humaid";
    private String phone = "32424";
    private String email = "humaidk@gmail.com";
    private String date = "29/10/2019";
    private int WarantyPeriod = 30;
    private int category = 0;
    private String amount= "50.5";

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
    EditText locationEditText;
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
        nameEditText = findViewById(R.id.warantyName);
        phoneEditText = findViewById(R.id.warantyPhone);
        emailEditText = findViewById(R.id.warantyEmail);
        warantyPeriodEditText = findViewById(R.id.warantyPeriod);
        amountEditText = findViewById(R.id.warantyAmount);
        locationEditText = findViewById(R.id.warantyLocation);
        myCalendar = Calendar.getInstance();
        nameEditText.setText("Humaid");
        phoneEditText.setText("34242");
        emailEditText.setText("humaidk2@gmail.com");
        warantyPeriodEditText.setText("50");
        amountEditText.setText("244");
        locationEditText.setText("UOWD Dubai");
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
        next = findViewById(R.id.nextPageButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddWaranteeForm2();
            }
        });



        //Date Picker Code
        myDate = findViewById(R.id.editText3);
        myDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
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
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.getDatePicker().updateDate(year, month, year);
                dpd.show();
            }
        });


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
        intent.putExtra("location", locationEditText.getText() + "");
        startActivity(intent);
    }
    //----------------------------- End of OpenAddWarantee -------------------------

}
