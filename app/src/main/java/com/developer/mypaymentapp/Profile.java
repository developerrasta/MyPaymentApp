package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.jar.Attributes;

public class Profile extends AppCompatActivity {

    ImageView pro;
    Button chg;
    TextView upi,uname;
    String userName;
    String showupikey,showuser;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pro = findViewById(R.id.profilepic);
        upi = findViewById(R.id.upiid);
        uname = findViewById(R.id.username);
        chg = findViewById(R.id.btnprofilepic);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        //fetching shared preference data
        userName = sharedPreferences.getString("UserName", "*****");

        //Toast.makeText(Profile.this,userName,Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lilac-wing.000webhostapp.com/Payment%20Application/Profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        //Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_obj = jsonArray.getJSONObject(i);
                                showupikey = json_obj.getString("upi");
                                showuser = json_obj.getString("username");
                                upi.setText(showupikey);
                                uname.setText(showuser);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("UpiKey",showupikey);
                                editor.apply();

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


                params.put("username", userName); //userName = data stored in shared preference

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(stringRequest);
    }


}

