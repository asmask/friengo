package com.example.asus.freingo.activities;


import android.os.Bundle;
import android.view.View;

import com.example.asus.freingo.R;
import com.example.asus.freingo.fragments.AccountFragment;
import com.example.asus.freingo.fragments.FavFragment;
import com.example.asus.freingo.fragments.HomeFragment;
import com.example.asus.freingo.fragments.LocationFragment;
import com.example.asus.freingo.fragments.NotifFragment;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class NavigationActivity extends AppCompatActivity {
    BubbleNavigationLinearView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setFragment(new HomeFragment());
        bubbleNavigationLinearView= findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 0:
                        setFragment(new HomeFragment());
                        return;
                    case 1:
                        setFragment(new AccountFragment());
                        return;
                    case 2:
                        setFragment(new LocationFragment());
                        return;
                    case 3:
                        setFragment(new FavFragment());
                        return;
                    case 4:
                        setFragment(new NotifFragment());
                        return;
                    default:
                        setFragment(new HomeFragment());
                        return;
                }
            }
        });

    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (R.id.content,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit ();
    }
}
