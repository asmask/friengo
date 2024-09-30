package com.example.asus.freingo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.asus.freingo.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ItemViewHolder>{
    private Context context;
    private List<Item> items;

    public CardAdapter(Context context, List<Item> items){
        this.context=context;
        this.items=items;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from (context);
        View view = layoutInflater.inflate (R.layout.place_card,null);
        return new ItemViewHolder (view);    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item= items.get (position);
        holder.item_title.setText (item.getTitle ());
        holder.item_desc.setText (item.getDesc ());
        holder.item_img.setImageDrawable (context.getResources ().getDrawable (item.getImg ()));
        holder.ratingBar.setRating (item.getRating ());
    }

    @Override
    public int getItemCount() {
        return items.size ();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView item_title,item_desc;
        ImageView item_img;
        RatingBar ratingBar;

        public ItemViewHolder(View itemView) {
            super (itemView);
            item_title= (TextView) itemView.findViewById (R.id.item_title);
            item_desc= (TextView) itemView.findViewById (R.id.item_desc);
            item_img= (ImageView) itemView.findViewById (R.id.item_img);
            ratingBar= (RatingBar) itemView.findViewById (R.id.ratingBar);

        }
    }
}
