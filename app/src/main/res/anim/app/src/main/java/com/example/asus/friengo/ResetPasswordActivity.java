package com.example.asus.friengo;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class ResetPasswordActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //action bar
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar ().setTitle ("Forgot password");
        getSupportActionBar ().setBackgroundDrawable (getDrawable (R.color.verteau));
        if(Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
