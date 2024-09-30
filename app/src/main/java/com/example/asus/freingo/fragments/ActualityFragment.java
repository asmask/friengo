package com.example.asus.freingo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.freingo.Adapter.CardDataImpl;
import com.example.asus.freingo.Adapter.CardListItemAdapter;
import com.example.asus.freingo.R;
import com.example.asus.freingo.remote.VolleyCallBack;
import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerCard;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;


public class ActualityFragment extends Fragment  {

    private ECPagerView ecPagerView;
    private View view;
    private List<ECCardData> dataset;
    List<ECCardData> data= new ArrayList<>();
    private String host;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_actuality, container, false);
        // Get pager from layout
        ecPagerView = (ECPagerView) view.findViewById(R.id.ec_pager_element);
        host=this.getString(R.string.host);

        getTopPlaces(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                Log.d("data",""+data.size());
                //dataset =new ArrayList<>(data.size());
                final ECPagerViewAdapter ecPagerViewAdapter = new ECPagerViewAdapter(getContext(), data) {
                    @Override
                    public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, final ListView list, ECCardData data) {
                        // Data object for current card
                        final CardDataImpl cardData = (CardDataImpl) data;

                        // Set adapter and items to current card content list
                        final List<String> listItems = cardData.getListItems();
                        final CardListItemAdapter listItemAdapter = new CardListItemAdapter(getContext(), listItems);
                        list.setAdapter(listItemAdapter);
                        // Also some visual tuning can be done here
                        list.setBackgroundColor(Color.WHITE);

                        // Here we can create elements for head view or inflate layout from xml using inflater service
                        /*TextView cardTitle = new TextView(getContext());
                        cardTitle.setText(cardData.getCardTitle());
                        cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20);*/
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;
                        //head.addView(cardTitle, layoutParams);
                        // Inflate main header layout and attach it to header root view
                        inflaterService.inflate(R.layout.simple_head, head);

                        // Set header data from data object

                        TextView title = (TextView) head.findViewById(R.id.title);
                        title.setText(cardData.getCardTitle());
                        TextView message = (TextView) head.findViewById(R.id.adresse);
                        message.setText(cardData.getAdresse());
                        TextView rating = (TextView) head.findViewById(R.id.rating);
                        rating.setText(""+cardData.getRating());
                        TextView phone = (TextView) head.findViewById(R.id.phone);
                        phone.setText(cardData.getPhone());
                        TextView name = (TextView) head.findViewById(R.id.time);
                        name.setText(cardData.getOpenning_time());
                        ImageView showOnMap=(ImageView) head.findViewById(R.id.showdir);
                        showOnMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cardData.getUrl()));
                                startActivity(mapIntent);
                            }
                        });
                        ImageView call=(ImageView) head.findViewById(R.id.call);
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse("tel:"+cardData.getPhone());
                                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                startActivity(intent);
                            }
                        });

                        /*ImageView img = (ImageView) head.findViewById(R.id.place_photo);
                        Picasso.with(getContext()).load(getPhotoById(cardData.getPlace_id())).into((img));
                        Drawable myDrawable = img.getDrawable();
                        int drawableResourceId = getResources().getIdentifier("myDrawable", "drawable",getActivity().getPackageName());
                        cardData.setHeadBackgroundResource(R.drawable.login_bk);*/


                    }
                };
                ecPagerView.setPagerViewAdapter(ecPagerViewAdapter);

                // Add background switcher to pager view
                ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) view.findViewById(R.id.ec_bg_switcher_element));

                // Directly modifying dataset
                data.remove(2);
                ecPagerViewAdapter.notifyDataSetChanged();

            }
        });
        return view;
    }



    public synchronized String getPhotoById(int place_id){
        final String[] photo = new String[1];
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/admin/TopPlaces/?place_id=" + place_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        photo[0] =response.optString("photo_reference");
                        Log.d("photo",String.valueOf(photo[0]));

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


    public  void getTopPlaces(final VolleyCallBack callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,host+"/admin/TopPlaces/",null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray;
                try {
                    jsonArray = response.getJSONArray("result");
                    Log.d("top",jsonArray.toString());
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject obj;
                        obj = jsonArray.getJSONObject(a);

                        CardDataImpl cardData=new CardDataImpl(obj.optString("place_name"),R.drawable.food,R.drawable.food,
                                obj.optString("phone"),obj.optString("place_adresse"),obj.optString("opening_time"),obj.optDouble("rating"),obj.getString("url"));


                        data.add(a,cardData);
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
}
