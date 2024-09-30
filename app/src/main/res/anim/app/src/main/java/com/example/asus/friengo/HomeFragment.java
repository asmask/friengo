package com.example.asus.friengo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private CardView money_card,distance_card,actuality_card;
    private Animation anim;
    private View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate (R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        money_card = (CardView) getActivity ().findViewById (R.id.money_card);
        money_card.setOnClickListener (this);

        distance_card = (CardView) getActivity ().findViewById (R.id.distance_card);
        distance_card.setOnClickListener (this);

        actuality_card = (CardView) view.findViewById (R.id.actuality_card);
        actuality_card.setOnClickListener (this);


        anim= AnimationUtils.loadAnimation (getActivity (),R.anim.lefttoright);
        money_card.setAnimation (anim);
        distance_card.setAnimation (anim);
        actuality_card.setAnimation (anim);
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==distance_card.getId ()){

            setFragment (new MapFragment ());

        }else if (v.getId ()==money_card.getId ()){

            setFragment (new MoneyOptFragment ());

        }else if(v.getId () == actuality_card.getId ()){

            setFragment (new ActualityFragment ());
        }
    }
    public  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (R.id.content,fragment);
        //fragmentTransaction.show (new ActualityFragment ());
        fragmentTransaction.addToBackStack (null);
        fragmentTransaction.commit ();
    }
}
