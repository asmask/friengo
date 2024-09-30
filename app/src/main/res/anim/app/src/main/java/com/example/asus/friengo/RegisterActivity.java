package com.example.asus.friengo;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private Animation fromBottom;
    private Button register;
    private TextInputLayout username,email,pwd,phone,confpd;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,6}" +               //at least 4 characters
                    "$");
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //action bar
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar ().setBackgroundDrawable (getDrawable (R.color.verteau));
        if(Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //get the animation file
        fromBottom= AnimationUtils.loadAnimation (this,R.anim.from_bottom);

        username= (TextInputLayout) findViewById(R.id.username);
        username.setAnimation (fromBottom);
        email= (TextInputLayout) findViewById(R.id.email);
        email.setAnimation (fromBottom);
        phone= (TextInputLayout) findViewById(R.id.phone);
        phone.setAnimation (fromBottom);
        pwd= (TextInputLayout) findViewById(R.id.pwd);
        pwd.setAnimation (fromBottom);
        confpd= (TextInputLayout) findViewById(R.id.confpwd);
        confpd.setAnimation (fromBottom);
        register=(Button)findViewById(R.id.register);
        register.setAnimation (fromBottom);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput(v);
            }
        });
    }
    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
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
            username.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 6) {
            username.setError("Username too long");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = pwd.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pwd.setError("Field can't be empty");
            return false;
        }else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pwd.setError("Password too weak");
            return false;
        }else {
            pwd.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phoneInput = phone.getEditText().getText().toString().trim();

        if (phoneInput.isEmpty()) {
            phone.setError("Field can't be empty");
            return false;
        } else if (phoneInput.length() > 8 | phoneInput.length() > 8   ) {
            phone.setError("invalid phone number ");
            return false;
        }  else {
            phone.setError(null);
            return true;
        }
    }
    private boolean validateConfPassword() {
        String passwordInput = pwd.getEditText().getText().toString().trim();
        String confpwdInput = confpd.getEditText().getText().toString().trim();

        if (confpwdInput.isEmpty()) {
            confpd.setError("Field can't be empty");
            return false;
        } else if (!confpwdInput.equals(passwordInput)) {
            confpd.setError("Password not matchet");
            return false;
        }  else {
            confpd.setError(null);
            return true;
        }
    }


    public boolean confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword() | !validatePhone() | !validateConfPassword()) {
            return false;
        }

        String input = "Email: " + email.getEditText().getText().toString();
        input += "\n";
        input += "User name : " + username.getEditText().getText().toString();
        input += "\n";
        input += "Password: " + pwd.getEditText().getText().toString();
        input += "\n";
        input += "Phone: " + phone.getEditText().getText().toString();
        input += "\n";
        input += "Confirm Password: " + confpd.getEditText().getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        return true;
    }
}
