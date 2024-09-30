package com.example.asus.freingo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.asus.freingo.Adapter.Item;
import com.example.asus.freingo.R;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class PlacesFoundActivity extends AppCompatActivity {

    TextView data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_found);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        toolbar.setTitle("Places found");
        data=(TextView) findViewById(R.id.data);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            data.setText(bundle.getString("Location")+""+bundle.getString("transportType")+""+bundle.getInt("amount")+""+bundle.getInt("nbperson")+""+bundle.getStringArray("placesType").toString());
        }

    }
}
