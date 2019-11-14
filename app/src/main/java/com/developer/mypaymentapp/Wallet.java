package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Wallet extends AppCompatActivity implements PaymentResultListener {

    TextView amtview;
    EditText amt;
    Button add;
    String serialKey,showAmount;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        amtview = findViewById(R.id.amountview);
        amt = findViewById(R.id.amount);
        add = findViewById(R.id.addmoney);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        serialKey = sharedPreferences.getString("serialKey", "*****");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lilac-wing.000webhostapp.com/PaymentApplication/walletretrieve.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        //Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_obj = jsonArray.getJSONObject(i);
                                showAmount = json_obj.getString("wallet");
                                amtview.setText(showAmount);

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


                params.put("serialkey", serialKey); //serialKey = data stored in shared preference

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Wallet.this);
        requestQueue.add(stringRequest);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amt.getText().toString().equals("")) {
                    Toast.makeText(Wallet.this, "Please fill payment", Toast.LENGTH_LONG).show();
                    return;
                }
                startPayment();
            }
        });
    }

        public void startPayment() {
            /**
             * You need to pass current activity in order to let Razorpay create CheckoutActivity
             */
            final Activity activity = this;

            final Checkout co = new Checkout();

            try {
                JSONObject options = new JSONObject();
                options.put("name", "Razorpay Corp");
                options.put("description", "Demoing Charges");
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
                options.put("currency", "INR");

                String payment = amt.getText().toString();

                double total = Double.parseDouble(payment);
                total = total * 100;
                options.put("amount", total);

                JSONObject preFill = new JSONObject();
                preFill.put("email", "nirmal.mohanlal6@gmail.com");
                preFill.put("contact", "8129656368");

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onPaymentSuccess(String razorpayPaymentID) {
            Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lilac-wing.000webhostapp.com/PaymentApplication/wallet.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            //Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
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


                    params.put("serialkey", serialKey); //serialKey = data stored in shared preference
                    params.put("amount",amt.getText().toString());

                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(Wallet.this);
            requestQueue.add(stringRequest);

        }

        @Override
        public void onPaymentError(int code, String response) {
            try {
                Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("OnPaymentError", "Exception in onPaymentError", e);
            }
        }

}

