package com.example.asus.freingo.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.asus.freingo.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

public class ResetPwdActivity extends AppCompatActivity {

    private TextInputLayout email;
    private Button submit;
    private Dialog builder;
    private SpotsDialog dialog;
    private String URL,host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        getSupportActionBar().setTitle("Forgot password");
        host=this.getString(R.string.host);

        email = (TextInputLayout) findViewById(R.id.email);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateEmail()){
                    dialog = new SpotsDialog(ResetPwdActivity.this, R.style.Custom);
                    dialog.show();
                    resetPwd();
                    dialog.dismiss();
                }
            }
        });

    }

    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.email));
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.email));
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    private void resetPwd(){
        URL = host+"/users/resetPassword/";
        RequestQueue requestQueue = Volley.newRequestQueue(ResetPwdActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        buildRequestCompletedDialog(ResetPwdActivity.this).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if(error.networkResponse!=null){

                            if(error.networkResponse.statusCode==404){
                                Toast.makeText(ResetPwdActivity.this,"Email not exist",Toast.LENGTH_LONG).show();

                            }else if(error.networkResponse.statusCode==500){
                                Toast.makeText(ResetPwdActivity.this,"Somthing Wrong ",Toast.LENGTH_LONG).show();

                            }}


                        else if(error instanceof TimeoutError) {
                            Toast.makeText(ResetPwdActivity.this,"timeouerror",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {

                            buildDialog(ResetPwdActivity.this).show();

                        } else if (error instanceof AuthFailureError) {

                            Toast.makeText(ResetPwdActivity.this,"AuthFaliyreError",Toast.LENGTH_SHORT).show();

                        } else if (error instanceof ServerError) {

                            Toast.makeText(ResetPwdActivity.this,"ServerError",Toast.LENGTH_SHORT).show();

                        } else if (error instanceof NetworkError) {

                            Toast.makeText(ResetPwdActivity.this,"NetworkError",Toast.LENGTH_SHORT).show();

                        } else if (error instanceof ParseError) {

                            Toast.makeText(ResetPwdActivity.this,"ParseError",Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", email.getEditText().getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(postRequest);

    }
    public Dialog buildRequestCompletedDialog(Context c) {

        builder = new Dialog(c);
        builder.setCancelable(false);
        builder.setContentView(R.layout.send_success);
        Button dialogButton = (Button) builder.findViewById(R.id.btnOk);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                startActivity(new Intent(ResetPwdActivity.this,LoginActivity.class));
                finish();
            }
        });

        return builder;
    }
    public Dialog buildDialog(Context c) {

        builder = new Dialog(c);
        builder.setCancelable(false);
        builder.setContentView(R.layout.alert_network);
        Button dialogButton = (Button) builder.findViewById(R.id.btnRetry);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                finish();
                startActivity(new Intent(ResetPwdActivity.this,ResetPwdActivity.class));
            }
        });

        return builder;
    }
}
