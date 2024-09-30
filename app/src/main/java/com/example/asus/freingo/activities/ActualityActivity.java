package com.example.asus.freingo.activities;


import android.os.Bundle;



import com.example.asus.freingo.Adapter.CardAdapter;
import com.example.asus.freingo.Adapter.Item;
import com.example.asus.freingo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ActualityActivity extends AppCompatActivity {

    private CardAdapter itemAdapter;
    private List<Item> items;
    private RecyclerView item_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actuality);
        item_place=(RecyclerView) findViewById (R.id.item_place);
        items =new ArrayList<>();
        for (int i=0;i<6;i++){
            items.add (new Item ("Salade tunisienne","salde",R.drawable.food,3));
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,false);
        item_place.setLayoutManager (linearLayoutManager);
        item_place.setHasFixedSize (true);
        itemAdapter =new CardAdapter (this,items);
        item_place.setAdapter (itemAdapter);

    /*    android.os.Handler handler =new android.os.Handler ();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1ms = 100ms
                RecyclerView.ViewHolder viewHolderDefault = item_place.findViewHolderForAdapterPosition(0);

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
        final SnapHelper snapHelper=new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView (item_place);
        //animated scroll
        item_place.addOnScrollListener (new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);
                if(newState == SCROLL_STATE_IDLE){
                    View v=snapHelper.findSnapView (linearLayoutManager);
                    int pos =linearLayoutManager.getPosition (v);
                    RecyclerView.ViewHolder viewHolder = item_place.findViewHolderForAdapterPosition (pos);
                    LinearLayout item_layout = (LinearLayout) viewHolder.itemView.findViewById (R.id.item_layout);
                    item_layout.animate ().scaleY (1).scaleX (1).setDuration (350).setInterpolator (new AccelerateInterpolator ()).start ();

                }else {
                    View v=snapHelper.findSnapView (linearLayoutManager);
                    int pos =linearLayoutManager.getPosition (v);
                    RecyclerView.ViewHolder viewHolder = item_place.findViewHolderForAdapterPosition (pos);
                    LinearLayout item_layout = (LinearLayout) viewHolder.itemView.findViewById (R.id.item_layout);
                    item_layout.animate ().scaleY (0.7f).scaleX (0.7f).setDuration (350).setInterpolator (new AccelerateInterpolator ()).start ();

                }
            }
        });
*/
    }
}
