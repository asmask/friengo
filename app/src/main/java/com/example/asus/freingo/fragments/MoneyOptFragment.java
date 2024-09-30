package com.example.asus.freingo.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.Adapter.CardDataImpl;
import com.example.asus.freingo.Helper.DirectionJSONParser;
import com.example.asus.freingo.R;
import com.example.asus.freingo.activities.PlacesFoundActivity;
import com.example.asus.freingo.activities.ViewDirectionsActivity;
import com.example.asus.freingo.models.Myroutes;
import com.example.asus.freingo.remote.Common;
import com.example.asus.freingo.remote.IGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;


public class MoneyOptFragment extends Fragment implements View.OnClickListener, android.location.LocationListener {
    private View view;
    private IndicatorSeekBar amount_seekbar, person_seekbar;
    private RadioButton lounge, otherplace, coffshop, restau;
    private RadioButton foot, car, taxi, othertransport;
    private Button search;
    private int nbperson,amount;
    private Location mLastLocation;
    Myroutes myroutes=null;

    protected LocationManager locationManager;
    String transportType,placeType,transportMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_money_opt, container, false);
        //declaration

        lounge= view.findViewById(R.id.lounge) ;
        coffshop= view.findViewById(R.id.coffshop) ;
        restau= view.findViewById(R.id.restau) ;

        foot=view.findViewById(R.id.foot) ;
        car= view.findViewById(R.id.car) ;
        taxi= view.findViewById(R.id.taxi) ;

      amount=10;
      amount_seekbar=view.findViewById(R.id.amount_seekbar);
      amount_seekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
          @Override
          public void onSeeking(SeekParams seekParams) {}
          @Override
          public void onStartTrackingTouch(IndicatorSeekBar seekBar) {}
          @Override
          public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
              amount=seekBar.getProgress();
              Toasty.success(getContext(),""+amount,Toast.LENGTH_SHORT).show();
          }
      });
      nbperson=1;
      person_seekbar= view.findViewById(R.id.person_seekbar);
      person_seekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
          @Override
          public void onSeeking(SeekParams seekParams) {}
          @Override
          public void onStartTrackingTouch(IndicatorSeekBar seekBar) {}
          @Override
          public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
              nbperson=seekBar.getProgress();
              Toasty.success(getContext(),""+nbperson,Toast.LENGTH_SHORT).show();
          }
      });

        //Location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 2F,this);
        search= view.findViewById(R.id.search);
        search.setOnClickListener(this);

        return view;
    }

  @Override
  public void onClick(View v) {
      if(v.getId()==R.id.search){
            Toasty.info(getContext(),"Hello").show();
          //radioBox
          if(foot.isChecked()){
              transportType=foot.getText().toString();
              transportMode="walking";
          }else if(car.isChecked()){
              transportType=car.getText().toString();
              transportMode="driving";

          }else if(taxi.isChecked()){
              transportType=taxi.getText().toString();
              transportMode="drivng";

          }
          if(restau.isChecked()){
              placeType=restau.getText().toString();
          }else if(lounge.isChecked()){
              placeType=restau.getText().toString();
          }else if(coffshop.isChecked()){
              placeType=restau.getText().toString();
          }

          Location location = new Location(LocationManager.GPS_PROVIDER);
          location.setLatitude(35.859485);
          location.setLongitude(10.603125);

          Bundle bundle=new Bundle();

          bundle.putInt("amount",amount);
          bundle.putInt("nbperson",nbperson);
          bundle.putString("transportType",transportType);
          bundle.putString("transportMode",transportMode);
          bundle.putString("placeType",placeType);
          bundle.putDouble("latitude",mLastLocation.getLatitude());
          bundle.putDouble("longitude",mLastLocation.getLongitude());

          //set Fragmentclass Arguments
          PlacesFoundFragment foundFragment=new PlacesFoundFragment();
          foundFragment.setArguments(bundle);

          setFragment(foundFragment);



      }

  }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (R.id.content,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit ();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        Log.d("Location",mLastLocation.toString());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }


}
