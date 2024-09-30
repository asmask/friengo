package com.example.asus.freingo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.asus.freingo.activities.ViewPlaceActivity;
import com.example.asus.freingo.models.Photos;
import com.example.asus.freingo.R;
import com.example.asus.freingo.remote.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ASUS on 10/02/2019.
 */
//public  class   ImageViewAdapter{
 public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ItemViewHolder>{
   private Context context;
    private List<Photos> photos;

    public ImageViewAdapter(Context context, List<Photos> photos){
        this.context=context;
        this.photos=photos;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from (context);
        View view = layoutInflater.inflate (R.layout.photo_layout,null);
        return new ItemViewHolder (view);    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Photos photo= photos.get (position);
        //Picasso.with(this).load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000)).placeholder(R.drawable.empty_img).into(place_photo);

        //holder.place_img.setImageDrawable (context.getResources ().getDrawable (photo.getPhoto_reference()));
        Picasso.with(context).load(holder.getPhotoOfPlace(photo.getPhoto_reference(),150)).resize(150, 150).into(holder.place_img);

    }

    @Override
    public int getItemCount() {
        return photos.size ();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView place_img;

        public ItemViewHolder(View itemView) {
            super (itemView);
            place_img= (ImageView) itemView.findViewById (R.id.photo_holder);

        }
        private  String getPhotoOfPlace(String photo_references,int maxWidth) {
            StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
            url.append("?maxwidth="+maxWidth);
            url.append("&photoreference="+photo_references);
            url.append("&key=AIzaSyCNnT0tYYU9FUzc76y_SUuABFrWoJrQ4Bg");
            return url.toString();
        }
    }
}
