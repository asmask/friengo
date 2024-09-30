package com.example.asus.friengo;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.asus.friengo.Models.SliderAdapter;

public class SlidesActivity extends AppCompatActivity  {

    private ViewPager SlideViewPager;
    private LinearLayout DotLayout;
    private SliderAdapter sliderAdapter;
    private ImageView[] dots;
    private Button bt_start;
    private int [] layouts={R.layout.slide1,R.layout.slide2,R.layout.slide3,R.layout.slide4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides);
        SlideViewPager=(ViewPager)findViewById(R.id.viewpager);
        DotLayout=(LinearLayout)findViewById(R.id.dots);
        bt_start=(Button)findViewById(R.id.button_start);

        sliderAdapter=new SliderAdapter(layouts,this);
        SlideViewPager.setAdapter(sliderAdapter);
        createDots(0);

        SlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
                if(i == 3){
                    bt_start.setVisibility(View.VISIBLE);
                    bt_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SlidesActivity.this,LoginActivity.class));
                            finish();
                        }
                    });

                }else {
                    bt_start.setVisibility(View.INVISIBLE);
                }
                }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        if(Build.VERSION.SDK_INT >= 19){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    public void createDots(int curentpos){
        if(DotLayout!=null){
            DotLayout.removeAllViews();
            dots=new ImageView[layouts.length];
            for (int i=0;i<layouts.length;i++){
                dots[i]=new ImageView(this);
                if(i==curentpos){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.activedots));
                }else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.defaultdaots));
                }
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4,0,4,0);
                DotLayout.addView(dots[i],params);
            }
        }

    }


}
