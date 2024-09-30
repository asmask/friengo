package com.example.asus.freingo.activities;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.asus.freingo.R;
import com.example.asus.freingo.fragments.AccountFragment;
import com.example.asus.freingo.fragments.FavFragment;
import com.example.asus.freingo.fragments.HomeFragment;
import com.example.asus.freingo.fragments.LocationFragment;
import com.example.asus.freingo.fragments.NotifFragment;
import com.example.asus.freingo.remote.Common;


import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class TabActivity extends AppCompatActivity {

    private SpaceTabLayout tabLayout;
    private  List<Fragment> fragmentList;
    private ViewPager viewPager;
    public HomeFragment homeFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);

        homeFragment = new HomeFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new AccountFragment());
        fragmentList.add(new LocationFragment());
        fragmentList.add(new FavFragment());
        fragmentList.add(new NotifFragment());

        viewPager = (ViewPager) findViewById(R.id.contentviewpager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

        //FireBase
        //Common.curentToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d("FirebaseToken",Common.curentToken.toString());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }





}
