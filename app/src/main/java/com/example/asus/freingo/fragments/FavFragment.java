package com.example.asus.freingo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.asus.freingo.R;
import com.example.asus.freingo.activities.ViewPlaceActivity;

import androidx.fragment.app.Fragment;


public class FavFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_fav, container, false);

        return view;
    }


}
