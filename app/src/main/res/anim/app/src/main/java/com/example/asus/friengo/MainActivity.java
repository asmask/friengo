package com.example.asus.friengo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import static com.example.asus.friengo.R.*;

public class MainActivity extends AppCompatActivity {

    private HomeFragment home;
    private ProfileFragment profile;
    private MapFragment map;
    private FavorisFragment favoris;
    private NotificationsFragment notif;
    private BottomNavigationView navbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (layout.activity_main);

        if(Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        home=new HomeFragment ();
        profile=new ProfileFragment ();
        map=new MapFragment ();
        favoris=new FavorisFragment ();
        notif=new NotificationsFragment ();

        setFragment (home);
        getSupportActionBar ().setTitle ("Home");
        navbar=(BottomNavigationView)findViewById (id.navigation);
        //BottomNavigationView navigation = (BottomNavigationView) findViewById (id.navigation);
       // navigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener);
        getSupportActionBar ().setBackgroundDrawable (getDrawable (color.verteau));

        navbar.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case id.navigation_home:
                        setFragment (home);
                        getSupportActionBar ().setTitle ("Home");
                        return true;
                    case id.navigation_profile:
                        setFragment (profile);
                        getSupportActionBar ().setTitle ("Profile");
                        return true;
                    case id.navigation_map:
                        setFragment (map);
                        getSupportActionBar ().setTitle ("Distance option");
                        return true;
                    case id.navigation_favoris:
                        setFragment (favoris);
                        getSupportActionBar ().setTitle ("Favorites");
                        return true;
                    case id.navigation_notifications:
                        setFragment (notif);
                        getSupportActionBar ().setTitle ("Notifications");
                        return true;
                }
                return false;
            }

        });



    }
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (id.content,fragment);
        fragmentTransaction.commit ();
    }
    public  void setToolBarTheme(String title){
        getSupportActionBar ().setTitle (title);

    }



}
