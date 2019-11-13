package com.developer.mypaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Profile extends AppCompatActivity {

    ImageView pro;
    Button chg;
    TextView upi, uname;
    String userName,key,imagekey;
    String showupikey, showuser;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    SharedPreferences sharedPreferences;
    Bitmap bitmap;
    Intent j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pro = findViewById(R.id.profilepic);
        upi = findViewById(R.id.upiid);
        uname = findViewById(R.id.usernamee);
        chg = findViewById(R.id.btnprofilepic);

        //retrive intent data
        j=getIntent();

        key = j.getStringExtra("key"); //
        
        imagekey = j.getStringExtra("imagekey");

        Picasso.get().load(imagekey).into(pro);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        //fetching shared preference data
        userName = sharedPreferences.getString("UserName", "*****");

        //Toast.makeText(Profile.this,userName,Toast.LENGTH_SHORT).show();

        chg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();


            }
        });
    }
        private void showFileChooser () {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    pro.setImageBitmap(bitmap);
                    getStringImage(bitmap);
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


        private void uploadImage(){
            class UploadImage extends AsyncTask<Bitmap,Void,String> {

                ProgressDialog loading;
                RequestHandler rh = new RequestHandler();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Profile.this, "Uploading...", null,true,true);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Bitmap... params) {
                    Bitmap bitmap = params[0];
                    String uploadImage = getStringImage(bitmap);

                    HashMap<String,String> data = new HashMap<>();

                    data.put("dimage", uploadImage);
                    data.put("dkey",key);


                    String result = rh.sendPostRequest("https://lilac-wing.000webhostapp.com/PaymentApplication/upload.php",data);

                    return result;
                }
            }

            UploadImage ui = new UploadImage();
            ui.execute(bitmap);
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.wallet:
                //Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                Intent w = new Intent(getApplicationContext(),Wallet.class);
                startActivity(w);
                return true;

            case R.id.payment:
                Intent p = new Intent(getApplicationContext(),QRCodeShow.class);
                startActivity(p);
                return true;

            case R.id.qrcode:
                Intent q = new Intent(getApplicationContext(),QRCodeScanner.class);
                startActivity(q);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://lilac-wing.000webhostapp.com/Payment%20Application/Profile.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //If we are getting success from server
//                        //Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject json_obj = jsonArray.getJSONObject(i);
//                                showupikey = json_obj.getString("upi");
//                                showuser = json_obj.getString("username");
//                                upi.setText(showupikey);
//                                uname.setText(showuser);
//
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("UpiKey",showupikey);
//                                editor.apply();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //error handling
//                    }
//
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                //Adding parameters to request
//
//
//                params.put("username", userName); //userName = data stored in shared preference
//
//                //returning parameter
//                return params;
//            }
//        };
//
//        //Adding the string request to the queue
//        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
//        requestQueue.add(stringRequest);
    }




