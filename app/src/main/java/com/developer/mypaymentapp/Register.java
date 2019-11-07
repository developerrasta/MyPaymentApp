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

public class Register extends AppCompatActivity {

    Button reg,log;
    EditText name,phno,uname,upi,pword,cpword;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg = findViewById(R.id.register); //register button
        log = findViewById(R.id.login); //login button

        name = findViewById(R.id.name); //name
        phno = findViewById(R.id.phonenumber); //password
        uname = findViewById(R.id.username); //username
        upi = findViewById(R.id.upiid); //upi id
        pword = findViewById(R.id.password); //password
        cpword = findViewById(R.id.cpassword); //confirm password

        //start activity login
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LogIn.class);
                startActivity(i);


            }
        });



        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking wether edittext is empty and passwords are entered same
                if(!(name.getText().toString().isEmpty()||phno.getText().toString().isEmpty()||uname.getText().toString().isEmpty()||upi.getText().toString().isEmpty()||pword.getText().toString().isEmpty()||cpword.getText().toString().isEmpty())&&(pword.getText().toString().equals(cpword.getText().toString())))
                {
                    //storing values to database
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://lilac-wing.000webhostapp.com/Payment%20Application/Register.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //If we are getting success from server

                                    Toast.makeText(Register.this,response,Toast.LENGTH_LONG).show();



                                    try {
                                        JSONArray jsonArray=new JSONArray(response);
                                        for(int i=0;i<jsonArray.length();i++){
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

                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //Adding parameters to request

                            params.put("name",name.getText().toString()); //name=$_POST['name'];
                            params.put("phonenum",phno.getText().toString());
                            params.put("username",uname.getText().toString());
                            params.put("upiid",upi.getText().toString());
                            params.put("password",pword.getText().toString());

                            //returning parameter
                            return params;
                        }
                    };


                    //Adding the string request to the queue
                    RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
                    requestQueue.add(stringRequest);

                }
                else
                {
                    //if values are incorrect
                    new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("INCORRECT VALUES")
                            .show();

                }

            }
        });
    }
}
