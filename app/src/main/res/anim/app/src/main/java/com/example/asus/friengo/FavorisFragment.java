package com.example.asus.friengo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;

import com.example.asus.friengo.Models.Item;
import com.example.asus.friengo.Models.ItemAdapter;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static android.support.v7.widget.RecyclerView.*;


public class FavorisFragment extends Fragment   {
    private RecyclerView item_place,item_coffe_place,item_others_place;
    private ItemAdapter itemAdapter;
    private List<Item> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_favoris, container, false);
        item_place=(RecyclerView) view.findViewById (R.id.item_place);
        item_coffe_place=(RecyclerView) view.findViewById (R.id.item_coffe_place);

                items =new ArrayList<> ();
        for (int i=0;i<6;i++){
            items.add (new Item ("Salade tunisienne","salde",R.drawable.plat,3));
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager (getContext (),LinearLayoutManager.HORIZONTAL,false);
        item_place.setLayoutManager (linearLayoutManager);
        item_place.setHasFixedSize (true);

        final LinearLayoutManager linearLayoutManagerr = new LinearLayoutManager (getContext (),LinearLayoutManager.HORIZONTAL,false);
        item_coffe_place.setLayoutManager (linearLayoutManagerr);
        item_coffe_place.setHasFixedSize (true);

        itemAdapter =new ItemAdapter (getContext (),items);

        item_place.setAdapter (itemAdapter);
        item_coffe_place.setAdapter (itemAdapter);

        //timer for default  item

        android.os.Handler handler =new android.os.Handler ();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1ms = 100ms
                ViewHolder viewHolderDefault = item_place.findViewHolderForAdapterPosition(0);

                LinearLayout eventparentDefault = (LinearLayout)  viewHolderDefault.itemView.
                        findViewById(R.id.item_layout);
                eventparentDefault.animate().scaleY(1).scaleX(1).setDuration(350).
                        setInterpolator(new AccelerateInterpolator()).start();

                LinearLayout eventcategoryDefault = (LinearLayout)  viewHolderDefault.itemView.
                        findViewById(R.id.item_layout);
                eventcategoryDefault.animate().alpha(1).setDuration(300).start();

            }
        }, 100);

        //snapHelper eli yrakaz ken aala card view ya9fch fl west binatehom
        final SnapHelper snapHelper=new GravitySnapHelper (Gravity.START);
        snapHelper.attachToRecyclerView (item_place);
        //animated scroll
        item_place.addOnScrollListener (new OnScrollListener () {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);
                if(newState == SCROLL_STATE_IDLE){
                        View v=snapHelper.findSnapView (linearLayoutManager);
                        int pos =linearLayoutManager.getPosition (v);
                    ViewHolder viewHolder = item_place.findViewHolderForAdapterPosition (pos);
                    LinearLayout item_layout = (LinearLayout) viewHolder.itemView.findViewById (R.id.item_layout);
                    item_layout.animate ().scaleY (1).scaleX (1).setDuration (350).setInterpolator (new AccelerateInterpolator ()).start ();

                }else {
                    View v=snapHelper.findSnapView (linearLayoutManager);
                    int pos =linearLayoutManager.getPosition (v);
                    ViewHolder viewHolder = item_place.findViewHolderForAdapterPosition (pos);
                    LinearLayout item_layout = (LinearLayout) viewHolder.itemView.findViewById (R.id.item_layout);
                    item_layout.animate ().scaleY (0.7f).scaleX (0.7f).setDuration (350).setInterpolator (new AccelerateInterpolator ()).start ();

                }
            }
        });

        return view;
    }



}
