package com.example.asus.freingo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.freingo.models.Notification;
import com.example.asus.freingo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ASUS on 30/03/2019.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        private ArrayList<Notification> data;
        //Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView mTitle,mText;
            private ImageView imageView;
            RelativeLayout relativeLayout;

            public MyViewHolder(View itemView) {
                super(itemView);

                mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
                mText = (TextView) itemView.findViewById(R.id.txtText);
                imageView= itemView.findViewById(R.id.imageView);
            }
        }

 /*   public RecyclerViewAdapter(Context context,ArrayList<Notification> data) {
            this.data = data;
            this.context=context;
        }*/

    public RecyclerViewAdapter(ArrayList<Notification> data) {
        this.data = data;
    }


    @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mTitle.setText(data.get(position).getTitle());
            holder.mText.setText(data.get(position).getText());
            //Picasso.with(context).load(data.get(position).getImage()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Notification item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Notification> getData() {
        return data;
    }
}
