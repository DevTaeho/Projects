package com.example.dhfls.testmikepenzandviewpager.iconactivity.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerFixed extends ViewPager {
    public ViewPagerFixed(@NonNull Context context) {
        super(context);
    }

    public ViewPagerFixed(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try{
            return super.onInterceptTouchEvent(ev);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return false;
    }
}
