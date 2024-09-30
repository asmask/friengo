package com.example.asus.freingo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.freingo.R;
import com.example.asus.freingo.activities.PlacesFoundActivity;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.supercharge.shimmerlayout.ShimmerLayout;


public class HomeFragment extends Fragment {
    private View view;
    private CardView money_card,actuality_card,distance_card;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_home, container, false);

        money_card= view.findViewById(R.id.money_card);
        money_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             setFragment(new MoneyOptFragment());
            }


        });
        distance_card= view.findViewById(R.id.distance_card);
        distance_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LocationFragment());
            }


        });
        actuality_card= view.findViewById(R.id.actuality_card);
        actuality_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              setFragment(new ActualityFragment());
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (R.id.content,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit ();
    }

}
