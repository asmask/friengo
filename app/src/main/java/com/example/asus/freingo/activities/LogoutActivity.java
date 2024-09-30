package com.example.asus.freingo.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.freingo.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LogoutActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageView userpic;
    TextView username;
    Button connect;
    TextView athorAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        linearLayout=findViewById(R.id.logoutlay);
        connect=findViewById(R.id.ok);
        userpic=findViewById(R.id.userPic);
        username=findViewById(R.id.username);
        athorAccount=findViewById(R.id.athorAccount);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            username.setText(LoginActivity.getDefaultsData("lastname",this)+" "+LoginActivity.getDefaultsData("firstname",this));
            URL profilePic;
            if(LoginActivity.getDefaultsData("picture",this)!= null && LoginActivity.getDefaultsData("picture",this).length() > 0) {
                try {
                    Log.d("Urlpic",LoginActivity.getDefaultsData("picture",this).trim());
                    profilePic = new URL(LoginActivity.getDefaultsData("picture",this).trim());
                    Picasso.with(this).load(profilePic.toString()).placeholder(R.drawable.empty_img).into(userpic);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }


        connect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                LoginActivity.setDefaults("islogin",true,LogoutActivity.this);
                LoginActivity.setDefaults("session",false,LogoutActivity.this);
                startActivity(new Intent(LogoutActivity.this,NavigationActivity.class));
                LogoutActivity.this.finish();
            }
        });

        athorAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.setDefaults("islogin",false,LogoutActivity.this);
                LoginActivity.setDefaults("session",false,LogoutActivity.this);
                startActivity(new Intent(LogoutActivity.this,LoginActivity.class));
            }
        });

    }

    @Override
    protected void onPause() {
        username.setText(LoginActivity.getDefaultsData("lastname",this)+" "+LoginActivity.getDefaultsData("firstname",this));
        URL profilePic=null;
        if(LoginActivity.getDefaultsData("picture",this)!= null && LoginActivity.getDefaultsData("picture",this).length() > 0) {
            try {
                Log.d("Urlpic",LoginActivity.getDefaultsData("picture",this).trim());
                profilePic = new URL(LoginActivity.getDefaultsData("picture",this).trim());
                Picasso.with(this).load(profilePic.toString()).placeholder(R.drawable.empty_img).into(userpic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        username.setText(LoginActivity.getDefaultsData("lastname",this)+" "+LoginActivity.getDefaultsData("firstname",this));
        URL profilePic;
        if(LoginActivity.getDefaultsData("picture",this)!= null && LoginActivity.getDefaultsData("picture",this).length() > 0) {
            try {
                Log.d("Urlpic",LoginActivity.getDefaultsData("picture",this).trim());
                profilePic = new URL(LoginActivity.getDefaultsData("picture",this).trim());
                Picasso.with(this).load(profilePic.toString()).placeholder(R.drawable.empty_img).into(userpic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        username.setText(LoginActivity.getDefaultsData("lastname",this)+" "+LoginActivity.getDefaultsData("firstname",this));
        URL profilePic;
        if(LoginActivity.getDefaultsData("picture",this)!= null && LoginActivity.getDefaultsData("picture",this).length() > 0) {
            try {
                Log.d("Urlpic",LoginActivity.getDefaultsData("picture",this).trim());
                profilePic = new URL(LoginActivity.getDefaultsData("picture",this).trim());
                Picasso.with(this).load(profilePic.toString()).placeholder(R.drawable.empty_img).into(userpic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
