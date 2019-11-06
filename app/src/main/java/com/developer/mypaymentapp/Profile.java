package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    ImageView pro;
    Button chg;
    TextView upi,uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pro = findViewById(R.id.profilepic);
        upi = findViewById(R.id.upiid);
        uname = findViewById(R.id.username);
        chg = findViewById(R.id.btnprofilepic);


    }
}
