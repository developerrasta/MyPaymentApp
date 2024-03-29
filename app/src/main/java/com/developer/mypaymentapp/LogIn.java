package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LogIn extends AppCompatActivity {

    EditText uname,pword;
    Button log;
    String a,image;
    SharedPreferences sharedPreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        log = findViewById(R.id.login);

        uname = findViewById(R.id.username);
        pword = findViewById(R.id.password);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE); //MyPREFERENCES="MyPrefs"

                if(!(uname.getText().toString().isEmpty()||pword.getText().toString().isEmpty()))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://lilac-wing.000webhostapp.com/PaymentApplication/Login.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //retrive json data
                                        JSONArray jsonArray=new JSONArray(response);
                                        for(int i=0;i<jsonArray.length();i++){
                                            JSONObject json_obj = jsonArray.getJSONObject(i);
                                            a=json_obj.getString("serialkey"); //serialkey=column name in table
                                            image=json_obj.getString("image"); //image=column name in table

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(LogIn.this, response, Toast.LENGTH_LONG).show();
                                    if(response.contains("success")) {
                                        //If we are getting success from server

                                        //saving to shared preference
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("UserName", uname.getText().toString()); //UserName=key
                                        editor.apply();

                                        Intent i1= new Intent(getApplicationContext(),Profile.class);
                                        //passing data to another activity using intent


                                        i1.putExtra("key",a);
                                        i1.putExtra("imagekey",image);

                                        //the  data in a& image is stored in key & imagekey

                                        startActivity(i1);

                                    }
                                    else
                                    {
                                        Toast.makeText(LogIn.this,"invalid",Toast.LENGTH_LONG).show();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //error handling
                                }


                            }
                            )

                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //Adding parameters to request

                            params.put("username",uname.getText().toString());
                            params.put("password",pword.getText().toString());

                            //returning parameter
                            return params;
                        }
                    };
                    //Adding the string request to the queue
                    RequestQueue requestQueue = Volley.newRequestQueue(LogIn.this);
                    requestQueue.add(stringRequest);
                }

                else
                {
                    //if values are incorrect
                    new SweetAlertDialog(LogIn.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("INVALID CREDENTIALS")
                            .show();
                }
            }
        });

    }
}
