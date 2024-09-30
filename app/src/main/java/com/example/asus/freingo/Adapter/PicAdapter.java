package com.example.asus.freingo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.freingo.R;
import com.example.asus.freingo.models.MPlaces;
import com.example.asus.freingo.models.Photos;
import com.example.asus.freingo.models.PlaceDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PicAdapter extends RecyclerView.Adapter<PicViewHolder> {

    Context context;
    List<MPlaces> places;

    public PicAdapter(Context context, List<MPlaces> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public PicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.pic_layout,null);
        return new PicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PicViewHolder holder, int position) {
        MPlaces place=places.get(position);
        holder.title.setText(place.getPlace_name());
        holder.adresse.setText(place.getPlace_adresse());
        holder.time.setText(place.getOpening_time());
        holder.phone.setText(place.getPhone_number());
        holder.rating.setText(""+place.getRating());
        Picasso.with(context).load(place.getImg()).into(holder.place_img);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
