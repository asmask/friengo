package com.example.asus.freingo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.models.MyPlaces;
import com.example.asus.freingo.models.Photos;
import com.example.asus.freingo.models.PlaceDetail;
import com.example.asus.freingo.models.Results;
import com.example.asus.freingo.models.Reviews;
import com.example.asus.freingo.remote.Common;
import com.example.asus.freingo.remote.IGoogleAPIService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.asus.freingo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;

    private double latitude,longitude;
    private Marker mMarker;
    private View slideView,dim;


    Location mLastLocation;
    IGoogleAPIService mService;
    MyPlaces currentPlace;

    //New Location
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private LocationRequest mLocationRequest;
    private String adress;
    private Reviews[] reviews;
    private String rating;
    private String phone;
    private String isOpen;
    private boolean isopen=false;
    private String Url;
    private String website;
    private Photos[] photos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getPlaces();
        //Init Service
        mService= Common.getGoogleAPIService();
        //Request Runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                 switch (item.getItemId()){

                     case R.id.action_coffe:
                         neabryPlace("cafe");
                         break;
                     case R.id.action_restau:
                         neabryPlace("restaurant");
                         break;
                     case R.id.action_lounge:
                         neabryPlace("bar");
                         break;
                     default:
                         break;
                 }
                 return true;
            }
        });
        //init location
        buildLocationCallback();
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
        }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();
                if(mMarker != null)
                    mMarker.remove();

                latitude= mLastLocation.getLatitude();
                longitude= mLastLocation.getLongitude();

                LatLng latLng= new LatLng(latitude,longitude);

                MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Your position").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));;
                mMarker=mMap.addMarker(markerOptions);

                //Move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        };

    }
    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        //radius
        mLocationRequest.setInterval(50000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void neabryPlace(final String placeType) {

        mMap.clear();
        final String url= getUrl(latitude,longitude,placeType);

        mService.getNearByPlaces(url).enqueue(new Callback<MyPlaces>() {
            @Override
            public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                //remeber assign value fro curentplace
                currentPlace = response.body();
                if(response.isSuccessful()){



                    for(int i=0; i< response.body().getResults().length; i++){

                        MarkerOptions markerOptions = new MarkerOptions();
                        Results googlePlace=response.body().getResults()[i];


                        double lat= Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                        double lng= Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                        String placeName=googlePlace.getName();

                        Log.d("Response",googlePlace.toString());
                        LatLng latLng=new LatLng(lat,lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName);
                        if(placeType.equals("cafe")) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_cafee));
                        }else if(placeType.equals("restaurant")) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restau));
                        }else if(placeType.equals("bar")) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bar));
                        }else {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
                        }
                        markerOptions.snippet(String.valueOf(i));//assign index of marker
                        mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPlaces> call, Throwable t) {

            }
        });

    }

    private String getUrl(double latitude, double longitude, String placeType) {

        StringBuilder googleplacesUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleplacesUrl.append("location="+latitude+","+longitude);
        googleplacesUrl.append("&radius="+1000);
        googleplacesUrl.append("&type="+placeType);
        googleplacesUrl.append("&sensor=ture");
        googleplacesUrl.append("&key="+getResources().getString(R.string.google_maps_key));
        Log.d("Url",googleplacesUrl.toString());
        return  googleplacesUrl.toString();
    }

    private boolean checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);

            else {
                ActivityCompat.requestPermissions(this,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                        mMap.setMyLocationEnabled(true);
                        //init location
                        buildLocationCallback();
                        buildLocationRequest();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());

                    }
                }else{
                    Toasty.error(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Init Google play services
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                mMap.setMyLocationEnabled(true);

            }
        }else{


                mMap.setMyLocationEnabled(true);
            }

            //Make event click on marker
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getSnippet()!=null ) {
                    //When user select marker just get the result of place and assign to static variable
                    Common.currentResult = currentPlace.getResults()[Integer.parseInt(marker.getSnippet())];
                    Log.d("currentResult",currentPlace.getResults()[Integer.parseInt(marker.getSnippet())].toString());
                    //Start new Activity

                    startActivity(new Intent(MapsActivity.this, ViewPlaceActivity.class));
                }
                    return true;


            }
        });

        }


    public  void getPlaces(){
        final String url = "http://192.168.1.9:2019/frienGo/admin/places";
        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        response.optString("place_id");
                        Log.d("places",response.optString("place_id"));
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

}
