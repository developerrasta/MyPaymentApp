package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class QRCodeShow extends AppCompatActivity {

    ImageView qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_show);

        qr = findViewById(R.id.qrcode);
    }
}
