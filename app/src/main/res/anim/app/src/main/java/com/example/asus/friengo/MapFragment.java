package com.example.asus.friengo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


public class MapFragment extends Fragment {
    private TextView distance;
    private View view;
    private Button location;
    private Animation anim,anim2,anim3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =inflater.inflate (R.layout.fragment_map, container, false);
        distance=(TextView) view.findViewById (R.id.txt);
        location=(Button)view.findViewById (R.id.location);
        anim= AnimationUtils.loadAnimation (view.getContext (),R.anim.fadein);
        anim2= AnimationUtils.loadAnimation (view.getContext (),R.anim.from_bottom);
        anim3= AnimationUtils.loadAnimation (view.getContext (),R.anim.lefttoright);
        distance.setAnimation (anim2);
        view.findViewById (R.id.textView3).setAnimation (anim);
        view.findViewById (R.id.textView4).setAnimation (anim3);
        view.findViewById (R.id.location).setAnimation (anim3);

        distance.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction =getFragmentManager ().beginTransaction ();
                fragmentTransaction.replace (R.id.content,new ManualyMapFragment ());
                fragmentTransaction.show (new ManualyMapFragment ());
                fragmentTransaction.commit ();
            }
        });

        location.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (getActivity (),MapsActivity.class));
            }
        });


        return view;


    }


}
