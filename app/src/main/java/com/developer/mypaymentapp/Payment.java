package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Payment extends AppCompatActivity {

    Button send;
    EditText amount,name,note,upivirtualid;
    SharedPreferences sharedPreferences;
    String QRScanSerialKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        send = findViewById(R.id.send);
        amount = findViewById(R.id.amount);
        name = findViewById(R.id.name);
        note = findViewById(R.id.note);
        upivirtualid = findViewById(R.id.upi);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        QRScanSerialKey = sharedPreferences.getString("QRScanSerialKey", "*****");

    }
}
