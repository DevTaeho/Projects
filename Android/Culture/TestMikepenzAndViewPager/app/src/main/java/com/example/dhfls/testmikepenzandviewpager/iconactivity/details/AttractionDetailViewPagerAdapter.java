package com.example.dhfls.testmikepenzandviewpager.iconactivity.details;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dhfls.testmikepenzandviewpager.R;
import com.github.chrisbanes.photoview.PhotoView;

public class AttractionDetailViewPagerAdapter extends PagerAdapter {

    Activity activity;
    String images[];
    LayoutInflater inflater;

    public AttractionDetailViewPagerAdapter(Activity activity, String[] images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.attraction_detail_viewpager_item, container, false);

        PhotoView photoView;

        /**
         * This is for AttractionDetailActivity's PhotoView.
         *
         */
        photoView = (PhotoView) itemView.findViewById(R.id.attraction_detail_cardview_photoview);

        DisplayMetrics dis = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        photoView.setMinimumHeight(height);
        photoView.setMinimumWidth(width);

        Glide.with(activity.getApplicationContext())
                .load(images[position])
                .into(photoView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ((ViewPager) container).removeView((View)object);
    }
}
