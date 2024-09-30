package com.example.asus.friengo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextView logo,frgpwd,signup;
    CardView cardView;
    Button login,fblogin;
    EditText username,pwd;
    Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anim= AnimationUtils.loadAnimation (this,R.anim.bounce);

        logo=(TextView)findViewById(R.id.title);
        logo.setAnimation (anim);

        login=(Button)findViewById (R.id.login);
        login.setOnClickListener (this);

        fblogin=(Button)findViewById (R.id.login);

        frgpwd=(TextView)findViewById(R.id.frgpwd);
        frgpwd.setOnClickListener(this);

        signup=(TextView)findViewById(R.id.signup);
        signup.setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==frgpwd.getId()){
            startActivity(new Intent(this,ResetPasswordActivity.class));
        }else if(v.getId()==signup.getId()){
            startActivity(new Intent(this,RegisterActivity.class));
        }else if(v.getId ()==login.getId ()){
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
