package com.example.asus.freingo.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.R;
import com.example.asus.freingo.fragments.AccountFragment;
import com.example.asus.freingo.fragments.FavFragment;
import com.example.asus.freingo.fragments.HomeFragment;
import com.example.asus.freingo.fragments.LocationFragment;
import com.example.asus.freingo.fragments.MoneyOptFragment;
import com.example.asus.freingo.fragments.NotifFragment;
import com.example.asus.freingo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MoneyOptActivity extends AppCompatActivity {

    public SpaceTabLayout tabLayout;
    private String registerToken,token,fbtoken,host="http://192.168.1.9:2019/frienGo";
    public static User user=null;
    public static String firstname,lastname,email,password,phone;
    public static   List<Fragment> fragmentList;
    public static ViewPager viewPager;
    public HomeFragment homeFragment ;
    public  MoneyOptFragment moneyOptFragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        homeFragment = new HomeFragment();
        moneyOptFragment = new MoneyOptFragment() ;

        fragmentList = new ArrayList<>();
        fragmentList.add(moneyOptFragment);
        fragmentList.add(new AccountFragment());
        fragmentList.add(new LocationFragment());
        fragmentList.add(new FavFragment());
        fragmentList.add(new NotifFragment());




        viewPager = (ViewPager) findViewById(R.id.contentviewpager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

        tabLayout.setTabOneOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyOptActivity.this, TabActivity.class));
            }
        });
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }





}
