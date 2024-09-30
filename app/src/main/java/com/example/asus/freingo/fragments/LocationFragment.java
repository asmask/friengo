package com.example.asus.freingo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asus.freingo.R;
import com.example.asus.freingo.activities.MapsActivity;

import androidx.fragment.app.Fragment;


public class LocationFragment extends Fragment {

    Button map_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_location, container, false);
        map_btn=v.findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });
        return v;
    }


}
