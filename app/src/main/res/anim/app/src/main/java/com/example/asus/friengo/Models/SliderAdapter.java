package com.example.asus.friengo.Models;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ASUS on 06/02/2019.
 */

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private int[] layouts;
    private LayoutInflater layoutInflater;

    public SliderAdapter (int[] layouts,Context context){
        this.context=context;
        this.layouts=layouts;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
       return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=(View)object;
        container.removeView(view);
    }
}
