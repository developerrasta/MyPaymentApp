package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Wallet extends AppCompatActivity {

    TextView amtview;
    EditText amt;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        amtview = findViewById(R.id.amountview);
        amt = findViewById(R.id.amount);
        add = findViewById(R.id.addmoney);

    }
}
