package com.example.asus.freingo.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.freingo.R;

import androidx.recyclerview.widget.RecyclerView;

public class PicViewHolder extends RecyclerView.ViewHolder {


    ImageView place_img;
    TextView title,adresse,phone,time,rating;


    public PicViewHolder(View itemView) {
        super(itemView);
        place_img=(ImageView) itemView.findViewById(R.id.place_img);
        title=(TextView) itemView.findViewById(R.id.title);
        adresse=(TextView) itemView.findViewById(R.id.adresse);
        phone=(TextView) itemView.findViewById(R.id.phone);
        time=(TextView) itemView.findViewById(R.id.time);
        rating=(TextView) itemView.findViewById(R.id.rating);
    }
}
