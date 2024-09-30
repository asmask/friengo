package com.example.asus.freingo.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.asus.freingo.R;
import com.example.asus.freingo.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button login,fb;
    private EditText username, pwd;
    private TextView signup,frgpwd;
    private SpotsDialog dialog;
    private Dialog builder;
    private String URL,token,host;
    public static boolean isLogin=false,session=false;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public  String picture, firstname,lastname,email,password,phone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        //host url
        host=this.getString(R.string.host);

        //wifi is active
        if(!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();

        //get values
        isLogin=getDefaults("islogin",this);
        session=getDefaults("session",this);

        //page animation
        myanimation();

        //if is login show home page
        if(isLogin){
            startActivity(new Intent(LoginActivity.this,NavigationActivity.class));
            finish();
        }

        //if session closed show logout page
        if(session){
            startActivity(new Intent(LoginActivity.this,LogoutActivity.class));
            finish();
        }


        //items
        username= findViewById(R.id.username);
        pwd= findViewById(R.id.pwd);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        signup= findViewById(R.id.signup);
        signup.setOnClickListener(this);

        frgpwd= findViewById(R.id.frgpwd);
        frgpwd.setOnClickListener(this);

        //fb permission and access token
        fb = findViewById(R.id.fb);
        loginButton = findViewById(R.id.login_button);

        callbackManager= CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                disconnectFromFacebook();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.d("facebook_accessToken",accessToken);
                facebookLogin(accessToken);
            }

            @Override
            public void onCancel() {
                Toasty.info(LoginActivity.this,"Facebook login cancled");
            }

            @Override
            public void onError(FacebookException error) {
                Toasty.error(LoginActivity.this,"Whoops looks like something went wrong").show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //to pass login results to the LoginManager via callbackManager and avoid errors.
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==login.getId()){
            if(username.getText().toString().equals("")&&pwd.getText().toString().equals("")){
                YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.username));
                YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.pwd));
            }else if(username.getText().toString().equals("")){
                YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.username));
            }else if(pwd.getText().toString().equals("")){
                YoYo.with(Techniques.Shake).duration(400).repeat(0).playOn( findViewById(R.id.pwd));
            }else {
                dialog = new SpotsDialog(LoginActivity.this, R.style.CustomCastTheme);
                dialog.show();
                login();
                dialog.dismiss();
            }
        }else if(view.getId()==signup.getId()){
            startActivity(new Intent(this,RegisterActivity.class));
        }else if(view.getId()==fb.getId()){
            loginButton.performClick();
        }else{
            startActivity(new Intent(this,ResetPwdActivity.class));
        }
    }

    //facebook_user picture url
    public String getUserDetailFromFB(){
        final java.net.URL[] pic = new URL[1];
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),new  GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    pic[0] =new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email");
        request.setParameters(parameters);
        request.executeAsync();

        return pic[0].toString();
    }

    //verif network
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }

    //network alert dialog
    public Dialog buildDialog(Context c) {

        builder = new Dialog(c);
        builder.setCancelable(false);
        builder.setContentView(R.layout.alert_network);
        Button dialogButton = builder.findViewById(R.id.btnRetry);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                finish();
                startActivity(new Intent(LoginActivity.this,LoginActivity.class));
            }
        });

        return builder;
    }

    public synchronized void login(){
        URL = host+"/users/login";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                             token=result.getString("token");
                                GetUserData(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isLogin=true;
                        setDefaults("islogin",isLogin,LoginActivity.this);
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class).putExtra("token",token));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if(error.networkResponse!=null){

                        if(error.networkResponse.statusCode==404){
                            Toasty.error(LoginActivity.this,"Username not exist").show();
                        }else if(error.networkResponse.statusCode==403){
                            Toasty.error(LoginActivity.this,"Wrong password").show(); }
                        }else if(error instanceof TimeoutError) {
                            Toast.makeText(LoginActivity.this,"timeouerror",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            buildDialog(LoginActivity.this).show();
                            //par ici to remove
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(LoginActivity.this,"AuthFaliyreError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(LoginActivity.this,"ServerError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(LoginActivity.this,"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(LoginActivity.this,"ParseError",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("password", pwd.getText().toString());
                params.put("username", username.getText().toString());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    private synchronized void facebookLogin(final String access_token){

        URL = host+"/users/login/facebook";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject result = new JSONObject(response);
                            token=result.getString("token");
                            GetUserData(token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isLogin=true;
                        setDefaults("islogin",isLogin,LoginActivity.this);
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class).putExtra("fbtoken",token));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Facebook_login_Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", access_token);
                return params;
            }
        };
        requestQueue.add(postRequest);

    }

    public static void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        LoginManager.getInstance().logOut();

    }

    private synchronized void addPic(String access_token,String url){

        URL = host+"/users/login/facebook/pic";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("ASMAAAAA", response.toString());
                        try {
                            JSONObject result = new JSONObject(response);
                            token=result.getString("token");
                            GetUserData(token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isLogin=true;
                        setDefaults("islogin",isLogin,LoginActivity.this);

                        Log.d("hihihihihihi",token);

                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class).putExtra("fbtoken",token));
                        Log.d("sqsd",token);

                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("ErrorResponse", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("pic",getUserDetailFromFB() );

                return params;
            }
        };
        requestQueue.add(postRequest);

    }

    public synchronized void GetUserData(final String tokenn){

        //used in facbooklogin and logn fn
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/users/getUser/?token=" + tokenn,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("UserData",response.toString());

                        firstname=response.optString("firstname");
                        lastname=response.optString("lastname");
                        password=response.optString("password");
                        email=response.optString("email");
                        phone=response.optString("phone");
                        picture=response.optString("picture");
                        String id=response.optString("id");

                        if(picture==null ){ setDefaultsData("picture","",LoginActivity.this); }

                        user = new User(response.optInt("id"),response.optString("username"),response.optString("firstname"),
                                response.optString("lastname"),response.optString("phone"),response.optString("email"),
                                response.optString("password"),response.optString("picture"),response.optDouble("longitude"),
                                response.optDouble("latitude"));

                        setDefaultsData("user",user.toString(),LoginActivity.this);
                        setDefaultsData("lastname",lastname,LoginActivity.this);
                        setDefaultsData("firstname",firstname,LoginActivity.this);
                        setDefaultsData("password",password,LoginActivity.this);
                        setDefaultsData("email",email,LoginActivity.this);
                        setDefaultsData("phone",phone,LoginActivity.this);
                        setDefaultsData("picture",picture,LoginActivity.this);
                        setDefaultsData("id",id,LoginActivity.this);


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

    public static void setDefaultsData(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public synchronized static String getDefaultsData(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
    public static void setDefaults(String key, boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static boolean getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }
    private void myanimation() {

        findViewById(R.id.logo_img).setAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.bounce));
        findViewById(R.id.linearLayout).setAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.fadein));
       /* YoYo.with(Techniques.SlideInUp)
                .duration(1000)
                .repeat(0)
                .playOn( findViewById(R.id.logo_img));
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn( findViewById(R.id.linearLayout));*/
    }
    private void printKeyHash() {
        try {

            PackageInfo info=getPackageManager().getPackageInfo("com.example.asus.freingo", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



}
