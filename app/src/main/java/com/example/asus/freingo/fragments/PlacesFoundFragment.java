package com.example.asus.freingo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.Adapter.CardAdapter;
import com.example.asus.freingo.Adapter.CardDataImpl;
import com.example.asus.freingo.Adapter.Item;
import com.example.asus.freingo.Adapter.PicAdapter;
import com.example.asus.freingo.R;
import com.example.asus.freingo.models.MPlaces;
import com.example.asus.freingo.models.Myroutes;
import com.example.asus.freingo.remote.Common;
import com.example.asus.freingo.remote.IGoogleAPIService;
import com.example.asus.freingo.remote.VolleyCallBack;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.ramotion.expandingcollection.ECCardData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesFoundFragment extends Fragment {

    View view;
    TextView data,data2,txtdistance;
    private IGoogleAPIService mService;
    Myroutes myroutes=null;
    String transportMode,transportType,host;
    int nbp,montant;
    int distance;
    List<Integer> placesId= new ArrayList<>();
    List<MPlaces> placesFound= new ArrayList<>();
    RecyclerView recyclerView;
    PicAdapter picAdapter;

    //private String host="http://192.168.137.1:2019/frienGo";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_places_found, container, false);
        host=this.getString(R.string.host);

        mService= Common.getGoogleAPIServiceScalars();
        data= view.findViewById(R.id.data);
        data2= view.findViewById(R.id.data2);
        txtdistance= view.findViewById(R.id.distance);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Bundle bundle = getArguments();
        if(bundle != null){
            data.setText(bundle.getDouble("longitude")+""+bundle.getDouble("latitude")+""+bundle.getString("transportType")+""+bundle.getInt("amount")+"");
            data2.setText(bundle.getInt("nbperson")+""+bundle.getString("placeType"));
            transportMode=bundle.getString("transportMode");
            transportType=bundle.getString("transportType");
            montant=bundle.getInt("amount");
            nbp=bundle.getInt("nbperson");

        }
        Location userLocation = new Location(LocationManager.GPS_PROVIDER);
        userLocation.setLatitude(bundle.getDouble("latitude"));
        userLocation.setLongitude(bundle.getDouble("longitude"));

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(35.768193);
        location.setLongitude(10.816397);
        drawPath(userLocation, location,transportMode);
        Calcule();
        if(bundle.getString("placeType").equals("Restaurant")){
            getPlaces(Calcule(),"Main dish",new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d("places",""+placesId.toString());
                    for(int i=0;i<placesId.size();i++){
                        getPlacesById(placesId.get(i),new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                Log.d("Finalplaces",""+placesFound.toString());
                                picAdapter = new PicAdapter(getContext(),placesFound);
                                recyclerView.setAdapter(picAdapter);
                            }
                        });
                    }

                }
            });
            }else if(bundle.getString("placeType").equals("Coffe shop")){

        }else if(bundle.getString("placeType").equals("Lounge")){

        }

        Log.d("PhotoRef",getPhotoById(1, new VolleyCallBack() {
            @Override
            public void onSuccess() {

            }
        })
        );
        return view;
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
    private void drawPath(Location mLastLocation, Location location, String direction_mode){


        String origin = new StringBuilder(String.valueOf(mLastLocation.getLatitude())).append(",").append(String.valueOf(mLastLocation.getLongitude())).toString();
        String destination = new StringBuilder(String.valueOf(location.getLatitude())).append(",").append(String.valueOf(location.getLongitude())).toString();
        String urlDirection=getUrl(origin,destination,direction_mode);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,urlDirection,null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("RESULT",response.toString());

                myroutes = new Gson().fromJson(response.toString(),new TypeToken<Myroutes>(){}.getType());

                Log.d("DISTANCE",""+myroutes.routes.get(0).legs.get(0).distance.value);
                Log.d("DURATION",myroutes.routes.get(0).legs.get(0).duration.text);
                txtdistance.setText(myroutes.routes.get(0).legs.get(0).distance.text+" Duration : "+myroutes.routes.get(0).legs.get(0).duration.text);

                distance= myroutes.routes.get(0).legs.get(0).distance.value;
                setDefaultsData("distance",distance,getContext());
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequest);



    }
    private double Calcule(){
        double montantF = 0;
        if(transportType.equals("On foot")){
            
            montantF=montant/nbp;
            
            Log.d("montantF on foot", String.valueOf(montantF));
            data2.append(montantF+"");
            
        }else if(transportType.equals("By taxi")){
            
            int dis=getDefaultsData("distance",getContext());
            double fraisTaxi=((dis/86)*40)+540;
            double fraisTaxiDt=fraisTaxi/1000;
            
            montantF=(montant-fraisTaxiDt)/nbp;

            Log.d("montantF by taxi", String.valueOf(montantF));
            data2.append("frais de taxi"+fraisTaxiDt+" Montant final"+montantF);
            
        }else if(transportType.equals("By car")){
            
            montantF=montant/nbp;
            Log.d("montantF by car", String.valueOf(montantF));
            data2.append(montantF+"");
        }
        
        return montantF;
    }
    public static void setDefaultsData(String key, int value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public synchronized static int getDefaultsData(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (int) preferences.getInt(key,1);
    }
    public  void getPlaces(double price,String type,final VolleyCallBack callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/admin/GetPlaces?price="+price+"&type="+type,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray;
                try {
                    jsonArray = response.getJSONArray("result");
                    Log.d("PlacesFound",jsonArray.toString());
                   for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject obj;
                        obj = jsonArray.getJSONObject(a);

                       placesId.add(a,obj.optInt("place_id"));
                    }

                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }
    public  void getPlacesById(int place_id,final VolleyCallBack callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/admin/getPlaceById?id="+place_id,null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("PlaceResponse",response.toString());
                try {
                    JSONObject jsonObject=response.getJSONObject("place");
                    placesFound.add(new MPlaces(jsonObject.optInt("place_id"),jsonObject.optString("place_name"),
                            jsonObject.optDouble("latitude"),jsonObject.optDouble("longitude"),jsonObject.optString("place_adresse"),
                            jsonObject.optDouble("rating"),jsonObject.optString("url"),jsonObject.optString("website"),
                            jsonObject.optString("opening_time"),jsonObject.optString("phone")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callBack.onSuccess();


            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }
    public synchronized String getPhotoById(int place_id,final VolleyCallBack callBack){
        final String[] photo = new String[1];
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/admin/TopPlaces/?place_id=" + place_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        photo[0] =response.optString("photo_reference");
                        Log.d("photo",String.valueOf(photo[0]));
                        callBack.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
        return String.valueOf(photo[0]);
    }
}
