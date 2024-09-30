package com.example.asus.freingo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.freingo.Helper.DirectionJSONParser;
import com.example.asus.freingo.models.Myroutes;
import com.example.asus.freingo.models.Step;
import com.example.asus.freingo.remote.Common;
import com.example.asus.freingo.remote.IGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.asus.freingo.R;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDirectionsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location mLastLocation;
    Marker mCurrentMarker;
    String directionMode="walking";
    String mapType;
    Polyline polyline;
    IGoogleAPIService mService;
    Myroutes myroutes=null;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mService= Common.getGoogleAPIServiceScalars();
        buildLocationRequest();
        buildLocationCallback();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();

                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Your position").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
                mCurrentMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                //destination location marker
                LatLng destinationLatLng = new LatLng(Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()), Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));
                mCurrentMarker = mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(Common.currentResult.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));

                drawPath(mLastLocation, Common.currentResult.getGeometry().getLocation(),directionMode);
               }
            };

    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        //radius
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastLocation = location;

                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())).title("Your position").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
                mCurrentMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                //destination location marker
                LatLng destinationLatLng=new LatLng(Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));
                mCurrentMarker = mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(Common.currentResult.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
                BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.action_driving:
                                directionMode="driving";
                                drawPath(mLastLocation,Common.currentResult.getGeometry().getLocation(),directionMode);
                                break;
                            case R.id.action_walking:
                                directionMode="walking";
                                drawPath(mLastLocation,Common.currentResult.getGeometry().getLocation(),directionMode);

                                break;
                            case R.id.action_satellite:
                                if(item.getTitle().equals("Satellite")){
                                    item.setIcon(R.drawable.ic_plan);
                                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                    item.setTitle("Plan");
                                }else if(item.getTitle().equals("Plan")){
                                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    item.setIcon(R.drawable.ic_satellite_black_24dp);
                                    item.setTitle("Satellite");
                                }

                                break;
                            case R.id.action_micro:

                                item.setIcon(R.drawable.ic_mic_black_24dp);
                                item.setTitle("On");
                                String txt;
                                if(myroutes != null ){
                                    String plainText = "Hey nice to meet you";
                                    for(Step step:myroutes.routes.get(0).legs.get(0).steps){


                                        txt=step.html_instructions;
                                        plainText = Html.fromHtml(txt).toString();
                                        speak(plainText);
                                        Log.d("Texttttttttttt",plainText);

                                    }

                                }
                                break;
                            default:
                                drawPath(mLastLocation,Common.currentResult.getGeometry().getLocation(),directionMode);
                                break;
                        }
                        return true;
                    }
                });


            }
        });

    }
    private void speak(final String text){
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if(result== TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.d("speck","languge not supported");
                    }else {
                        textToSpeech.setSpeechRate(1.0f);
                        textToSpeech.setPitch(0.6f);
                        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);

                    }
                }


            }
        });
    }
    private String getUrl(String origin, String destination, String mode) {

        StringBuilder googleplacesUrl= new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleplacesUrl.append("origin="+origin);
        googleplacesUrl.append("&destination="+destination);
        googleplacesUrl.append("&mode="+mode);
        googleplacesUrl.append("&key="+getResources().getString(R.string.google_maps_key));
        Log.d("DirectionUrl",googleplacesUrl.toString());
        return  googleplacesUrl.toString();
    }
    private void drawPath(Location mLastLocation, com.example.asus.freingo.models.Location location,String direction_mode){

        //clear all polyline
        if(polyline != null){
            polyline.remove();
        }


        String origin = new StringBuilder(String.valueOf(mLastLocation.getLatitude())).append(",").append(String.valueOf(mLastLocation.getLongitude())).toString();
        String destination = new StringBuilder(location.getLat()).append(",").append(location.getLng()).toString();
        String urlDirection=getUrl(origin,destination,direction_mode);

        mService.getDirectionsWithModeTransport(urlDirection).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                myroutes = new Gson().fromJson(response.body(),new TypeToken<Myroutes>(){}.getType());
                new ParseTask().execute(response.body());
                Log.d("DirectionResponse",response.body());
                Log.d("Routessss",myroutes.routes.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });



    }

    private class ParseTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>> {


        AlertDialog watingDialog = new SpotsDialog(ViewDirectionsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            watingDialog.show();

        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0] );
                DirectionJSONParser parser = new DirectionJSONParser();
                routes =parser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            PolylineOptions polylineOptions = new PolylineOptions();
            ArrayList<LatLng> points ;
            for (int i=0;i<lists.size();i++){

                points = new ArrayList();


                List<HashMap<String,String>> path=lists.get(i);

                for (int j=0;j<path.size();j++){
                    HashMap<String,String> point= path.get(j);
                    double lat=Double.parseDouble(point.get("lat"));
                    double lng=Double.parseDouble(point.get("lng"));
                    LatLng positon= new LatLng(lat,lng);
                    points.add(positon);
                }
                polylineOptions.addAll(points).width(12).visible(true).color(R.color.colorPrimary);

            }
            if(polylineOptions!=null)
            {
                polyline =mMap.addPolyline(polylineOptions);
                watingDialog.dismiss();
            }

        }
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();

    }
}
