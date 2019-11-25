package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    EditText amt;
    Button pay;
    SharedPreferences sharedPreferences;
    String RecieverSerialKey,SenderSerialKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amt = findViewById(R.id.amount);
        pay = findViewById(R.id.send);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        RecieverSerialKey = sharedPreferences.getString("QRScanSerialKey", "*****");
        SenderSerialKey = sharedPreferences.getString("serialKey","*****");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lilac-wing.000webhostapp.com/PaymentApplication/transaction.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //If we are getting success from server
                                Toast.makeText(Payment.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json_obj = jsonArray.getJSONObject(i);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //error handling
                            }

                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding parameters to request


                        params.put("amount", amt.getText().toString());
                        params.put("payid", SenderSerialKey);
                        params.put("recid", RecieverSerialKey);
                        //returning parameter
                        return params;
                    }
                };

                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(Payment.this);
                requestQueue.add(stringRequest);


            }
        });



    }
}
