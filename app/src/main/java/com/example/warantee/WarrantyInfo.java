package com.example.warantee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WarrantyInfo extends AppCompatActivity {

    TextView sellerName, sellerPhone, sellerEmail, amount, date, warrantyPeriod, category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty_info);

        sellerName = findViewById(R.id.seller_name);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerEmail = findViewById(R.id.seller_email);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        warrantyPeriod = findViewById(R.id.warranty_period);
        category = findViewById(R.id.category);

    }
}
