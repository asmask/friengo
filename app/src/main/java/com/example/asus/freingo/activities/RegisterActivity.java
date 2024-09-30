package com.example.asus.freingo.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.asus.freingo.R;
import com.example.asus.freingo.models.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private TextInputLayout username, email, pwd, phone, confpd, firstname, lastname;
    private String URL,token,host;
    private SpotsDialog dialog;
    private Dialog builder;
    private boolean isLogin=false;
    public  SharedPreferences prefs;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,8}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Create an account");
        host=this.getString(R.string.host);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
            StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //User is login
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("islogin", false);

        username = findViewById(R.id.username);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pwd = findViewById(R.id.pwd);
        confpd = findViewById(R.id.confpwd);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmInput(view)){
                    dialog = new SpotsDialog(RegisterActivity.this, R.style.CustomCastTheme);
                    dialog.show();
                    register();
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
    private boolean validateUsername() {
        String usernameInput = username.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.username));
            username.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 6  ) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.username));
            username.setError("Username too long");
            return false;
        }  else if (usernameInput.length() < 4  ) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.username));
            username.setError("Username too short");
            return false;
        }else {
            username.setError(null);
            return true;
        }
    }
    private boolean validateFirstname() {
        String firstnameInput = firstname.getEditText().getText().toString().trim();

        if (firstnameInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.firstname));
            firstname.setError("Field can't be empty");
            return false;
        } else if (firstnameInput.length() > 6) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.firstname));
            firstname.setError("firstname too long");
            return false;
        }else if (firstnameInput.matches(".*\\d.*")) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.lastname));
            lastname.setError("firstname must not contains a number");
            return false;
        }else {
            firstname.setError(null);
            return true;
        }
    }
    private boolean validateLastname() {
        String lastnameInput = lastname.getEditText().getText().toString().trim();

        if (lastnameInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.lastname));
            lastname.setError("Field can't be empty");
            return false;
        } else if (lastnameInput.length() > 6) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.lastname));
            lastname.setError("Lastname too long");
            return false;
        }else if (lastnameInput.matches(".*\\d.*")) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.lastname));
            lastname.setError("Lastname must not contains a number");
            return false;
        }else {
            lastname.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = pwd.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.pwd));
            pwd.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.pwd));
            pwd.setError("Password length must be at least 4 characters and max 8 characters with no  white spaces ");
            return false;
        } else {
            pwd.setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        String phoneInput = phone.getEditText().getText().toString().trim();

        if (phoneInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.phone));
            phone.setError("Field can't be empty");
            return false;
        } else if (phoneInput.length() > 8 && phoneInput.length() < 8) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.phone));
            phone.setError("invalid phone number ");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }
    private boolean validateConfPassword() {
        String passwordInput = pwd.getEditText().getText().toString().trim();
        String confpwdInput = confpd.getEditText().getText().toString().trim();

        if (confpwdInput.isEmpty()) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.confpwd));
            confpd.setError("Field can't be empty");
            return false;
        } else if (!confpwdInput.equals(passwordInput)) {
            YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.confpwd));
            confpd.setError("Password not matchet");
            return false;
        } else {
            confpd.setError(null);
            return true;
        }
    }
    public boolean confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword() | !validatePhone() | !validateConfPassword()| !validateFirstname()| !validateLastname()) {
            return false;
        }
        return true;
    }

    public void register(){
        URL =host+"/utilisateurs/register";
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response;

                        try {
                            JSONObject result = new JSONObject(response);
                            token=result.getString("token");
                            GetUserData(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Register_response", response);
                        isLogin=true;
                        LoginActivity.setDefaults("islogin",isLogin,RegisterActivity.this);
                        buildWelcomeDialog(RegisterActivity.this).show();
                        startActivity(new Intent(RegisterActivity.this, NavigationActivity.class).putExtra("Registertoken",token));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Error.Response", error.toString());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(RegisterActivity.this,"timeouerror",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {

                            buildNoConnectionDialog(RegisterActivity.this).show();

                        } else if (error instanceof AuthFailureError) {

                            Toast.makeText(RegisterActivity.this,"AuthFaliyreError",Toast.LENGTH_SHORT).show();

                        } else if (error instanceof ServerError) {

                            Toast.makeText(RegisterActivity.this,"ServerError",Toast.LENGTH_SHORT).show();

                        } else if (error instanceof NetworkError) {

                            Toast.makeText(RegisterActivity.this,"NetworkError",Toast.LENGTH_SHORT).show();

                        } else if (error instanceof ParseError) {

                            Toast.makeText(RegisterActivity.this,"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getEditText().getText().toString());
                params.put("username", username.getEditText().getText().toString());
                params.put("nom", lastname.getEditText().getText().toString());
                params.put("prenom", firstname.getEditText().getText().toString());
                params.put("password", pwd.getEditText().getText().toString());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
    public synchronized void GetUserData(final String tokenn){
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/users/getUser/?token=" + tokenn,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("UserData",response.toString());

                        String firstname=response.optString("firstname");
                        String lastname=response.optString("lastname");
                        String password=response.optString("password");
                        String email=response.optString("email");
                        String phone=response.optString("phone");
                        String picture=response.optString("picture");
                        String id=response.optString("id");

                        if(picture==null ){ LoginActivity.setDefaultsData("picture","",RegisterActivity.this); }

                        User user = new User(response.optInt("id"),response.optString("username"),response.optString("firstname"),
                                response.optString("lastname"),response.optString("phone"),response.optString("email"),
                                response.optString("password"),response.optString("picture"),response.optDouble("longitude"),
                                response.optDouble("latitude"));

                        LoginActivity.setDefaultsData("user",user.toString(),RegisterActivity.this);
                        LoginActivity.setDefaultsData("lastname",lastname,RegisterActivity.this);
                        LoginActivity. setDefaultsData("firstname",firstname,RegisterActivity.this);
                        LoginActivity.setDefaultsData("password",password,RegisterActivity.this);
                        LoginActivity.setDefaultsData("email",email,RegisterActivity.this);
                        LoginActivity.setDefaultsData("phone",phone,RegisterActivity.this);
                        LoginActivity.setDefaultsData("picture",picture,RegisterActivity.this);
                        LoginActivity.setDefaultsData("id",id,RegisterActivity.this);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("User", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public Dialog buildWelcomeDialog(Context c) {

        builder = new Dialog(c);
        builder.setCancelable(false);
        builder.setContentView(R.layout.activity_welcome);
        TextView userName=(TextView)builder.findViewById(R.id.username);
        userName.setText(lastname.getEditText().getText().toString()+" "+firstname.getEditText().getText().toString());
        Button dialogButton = (Button) builder.findViewById(R.id.next);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                finish();
                startActivity(new Intent(RegisterActivity.this,NavigationActivity.class));
            }
        });

        return builder;
    }
    public Dialog buildNoConnectionDialog(Context c) {

        builder = new Dialog(c);
        builder.setCancelable(false);
        builder.setContentView(R.layout.alert_network);
        Button dialogButton =  builder.findViewById(R.id.btnRetry);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                finish();
                startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
            }
        });

        return builder;
    }
}

