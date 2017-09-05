package com.jyoung.honeystraw.base;

/**
 * Created by jyoung on 2017. 8. 17..
 */

import android.content.Context;

import android.support.v4.view.ViewPager;

import android.util.AttributeSet;

import android.view.MotionEvent;

import android.view.View;



public class VerticalViewPager extends ViewPager{

    private final String TAG = "VerticalViewPager";



    public VerticalViewPager(Context context) {

        this(context, null);

    }



    public VerticalViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

        init();

    }



    private void init() {

        setPageTransformer(true, new VerticalPageTransformer());

        setOverScrollMode(OVER_SCROLL_NEVER);

    }



    private class VerticalPageTransformer implements ViewPager.PageTransformer {



        @Override

        public void transformPage(View view, float position) {



            if (position < -1) {

                view.setAlpha(0);



            } else if (position <= 1) {

                view.setAlpha(1);

                view.setTranslationX(view.getWidth() * -position);

                float yPosition = position * view.getHeight();

                view.setTranslationY(yPosition);

            } else {

                view.setAlpha(0);

            }

        }

    }



    @Override

    public boolean onInterceptTouchEvent(MotionEvent ev){

        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));

        swapXY(ev);

        return intercepted;

    }



    @Override

    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(swapXY(ev));

    }



    private MotionEvent swapXY(MotionEvent ev) {

        float width = getWidth();

        float height = getHeight();



        float newX = (ev.getY() / height) * width;

        float newY = (ev.getX() / width) * height;



        ev.setLocation(newX, newY);



        return ev;

    }

}