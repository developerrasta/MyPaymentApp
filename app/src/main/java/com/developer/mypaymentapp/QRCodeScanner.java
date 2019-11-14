package com.developer.mypaymentapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScanner extends AppCompatActivity {

    Button scan;
    private IntentIntegrator qrScan;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        scan = findViewById(R.id.scan);

        qrScan = new IntentIntegrator(this);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(this,"Result not found",Toast.LENGTH_LONG).show();
            }
            else
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("QRScanSerialKey",result.getContents());
                editor.apply();

                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();

                Intent next = new Intent(getApplicationContext(),Payment.class);
                startActivity(next);

            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
