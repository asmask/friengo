package com.example.asus.freingo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.asus.freingo.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        YoYo.with(Techniques.StandUp)
                .duration(2000)
                .repeat(0)
                .playOn( findViewById(R.id.logo));

        //Splash duration
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 4500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreenActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }


}
