package com.example.asus.freingo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.Adapter.ImageViewAdapter;
import com.example.asus.freingo.Adapter.PicAdapter;
import com.example.asus.freingo.R;
import com.example.asus.freingo.models.Photos;
import com.example.asus.freingo.models.PlaceDetail;
import com.example.asus.freingo.remote.Common;
import com.example.asus.freingo.remote.IGoogleAPIService;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewPlaceActivity extends AppCompatActivity {

    ImageView place_photo;
    RatingBar ratingBar;
    TextView opning_hours,nbRating,place_adress,place_name,phone,website,nbReviews;
    Button btnViewOnMap,btnViewDirection,add;

    RecyclerView photo_recycle;
    ImageViewAdapter imageViewAdapter;
    List<Photos> photosList = new ArrayList<>();
    IGoogleAPIService mService;
    PlaceDetail mPlace;
    LinearLayoutManager linearLayoutManager;
    boolean isopen=true;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

        mService = Common.getGoogleAPIService();
        place_photo = (ImageView) findViewById(R.id.place_photo);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        place_adress=(TextView) findViewById(R.id.place_adress);
        place_name=(TextView) findViewById(R.id.place_name);
        phone=(TextView) findViewById(R.id.phone);
        website=(TextView) findViewById(R.id.website);
        nbReviews=(TextView) findViewById(R.id.nbReviews);
        btnViewOnMap=(Button) findViewById(R.id.btn_show_map);
        opning_hours=(TextView) findViewById(R.id.place_open_hour);
        btnViewDirection=(Button) findViewById(R.id.btn_view_direction);
        photo_recycle=(RecyclerView) findViewById(R.id.photo_recycle);
        add=(Button) findViewById(R.id.add);
        place_adress.setText("");
        place_adress.setText("");
        opning_hours.setText("");
        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });
        btnViewDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directionMapIntent= new Intent(ViewPlaceActivity.this,ViewDirectionsActivity.class);
                startActivity(directionMapIntent);
            }
        });
        linearLayoutManager = new LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,false);
        photo_recycle.setLayoutManager (linearLayoutManager);
        photo_recycle.setHasFixedSize (true);



        //photo
        if(Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length > 0){
            //getPhotos return array so we will take the first item


            Picasso.with(this).load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),150)).resize(150,150).placeholder(R.drawable.empty_img).into(place_photo);

        }

        //Rating
        if(Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating())){

            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }else{
            ratingBar.setVisibility(View.GONE);
        }
        //Opning_hours
        if(Common.currentResult.getOpening_hours() != null){

            opning_hours.setText("Open now :"+Common.currentResult.getOpening_hours().getOpen_now());
        }else{
            opning_hours.setVisibility(View.GONE);
        }

        //User service to fetch Adress and name
      //  place_Details();().getOpenin

                mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                        .enqueue(new Callback<PlaceDetail>() {
                            @Override
                            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                                if (response != null) {
                                    JsonParser jParser = new JsonParser();

                                    mPlace = response.body();


                                    Log.d("mPlace",response.toString());
                                    if (mPlace.getResult().getWebsite() == null ) website.setVisibility(View.GONE);
                                    website.setText(mPlace.getResult().getWebsite());
                                    phone.setText(mPlace.getResult().getFormatted_phone_number());
                                    if(mPlace.getResult().getReviews() !=null && mPlace.getResult().getReviews().length>0)
                                        nbReviews.setText(String.valueOf(mPlace.getResult().getReviews().length));
                                    place_adress.setText(mPlace.getResult().getFormatted_address());
                                    place_name.setText(mPlace.getResult().getName());
                                    //  Log.d("openingTime",mPlace.getResult().getOpening_hours().getWeekday_text().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceDetail> call, Throwable t) {

                            }
                        });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mPlace.getResult().getOpening_hours().getOpen_now()==null || mPlace.getResult().getOpening_hours().getOpen_now().equals("false")) isopen=false;
                if (mPlace.getResult().getWebsite() == null ){
                    addPlace(Common.currentResult.getPlace_id(),mPlace.getResult().getName(),Double.parseDouble(mPlace.getResult().getGeometry().getLocation().getLat()),
                            Double.parseDouble(mPlace.getResult().getGeometry().getLocation().getLng()),mPlace.getResult().getFormatted_address(),
                            Float.parseFloat(mPlace.getResult().getRating()),mPlace.getResult().getUrl(),"",
                            isopen,mPlace.getResult().getFormatted_phone_number());
                }else {
                    addPlace(Common.currentResult.getPlace_id(),mPlace.getResult().getName(),Double.parseDouble(mPlace.getResult().getGeometry().getLocation().getLat()),
                            Double.parseDouble(mPlace.getResult().getGeometry().getLocation().getLng()),mPlace.getResult().getFormatted_address(),
                            Float.parseFloat(mPlace.getResult().getRating()),mPlace.getResult().getUrl(),mPlace.getResult().getWebsite(),
                            isopen,mPlace.getResult().getFormatted_phone_number());
                }

            }
        });


    }
    /*public void displayPhotos(List<Photos> photos){
        PicAdapter picAdapter = new PicAdapter(mPlace,this,photos);
        photo_recycle.setAdapter(picAdapter);
    }*/
    public  void place_Details(){
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        if (response != null) {
                            JsonParser jParser = new JsonParser();

                            mPlace = response.body();


                            Log.d("mPlace",response.toString());
                            if (mPlace.getResult().getWebsite() == null ) website.setVisibility(View.GONE);
                            website.setText(mPlace.getResult().getWebsite());
                            phone.setText(mPlace.getResult().getFormatted_phone_number());
                            if(mPlace.getResult().getReviews() !=null && mPlace.getResult().getReviews().length>0)
                                nbReviews.setText(String.valueOf(mPlace.getResult().getReviews().length));
                            place_adress.setText(mPlace.getResult().getFormatted_address());
                            place_name.setText(mPlace.getResult().getName());

                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }
    private  String getPlaceDetailUrl(String place_id) {
        StringBuilder url=new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key="+getResources().getString(R.string.google_maps_key));
        return url.toString();
    }

    //load place photo
    private  String getPhotoOfPlace(String photo_references,int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_references);
        url.append("&key="+getResources().getString(R.string.google_maps_key));
        return url.toString();
    }
    public  synchronized void getPlaces(){
        final String url = "http://192.168.1.9:2019/frienGo/admin/places";
        RequestQueue queue = Volley.newRequestQueue(ViewPlaceActivity.this);
// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        response.optString("place_id");
                        Log.d("hhhhhhhh",response.optString("place_id"));
                        try {
                            Log.d("ahhhrray",response.getJSONArray("places").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public synchronized void addPlace(final String place_id, final String placeName, final Double latitude, final Double longitude,
                                      final String place_adresse, final Float rating, final String url, final String website, final Boolean open_now, final String phone_number){
        final String URL = "http://192.168.1.118:2019/frienGo/admin/add/place/";

        RequestQueue requestQueue = Volley.newRequestQueue(ViewPlaceActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("addplace",response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("erroraddplace",error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("place_id",place_id );
                params.put("place_name",placeName );
                params.put("latitude",String.valueOf(latitude) );
                params.put("longitude",String.valueOf(longitude) );
                params.put("place_adresse",place_adresse );
                params.put("rating",String.valueOf(rating));
                params.put("url",url );
                params.put("website", website );
                params.put("open_now",String.valueOf(open_now));
                params.put("phone_number",phone_number);

                return params;
            }
        };
        requestQueue.add(postRequest);

    }
}
