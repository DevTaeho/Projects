package com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dhfls.testmikepenzandviewpager.R;

public class HotelRecyclerViewItemHolder extends RecyclerView.ViewHolder {

    TextView hotelTileTextView;
    ImageView hotelImageView;

    public HotelRecyclerViewItemHolder(View itemView) {
        super(itemView);

        if(itemView != null){
            hotelTileTextView = (TextView) itemView.findViewById(R.id.hotel_card_view_image_title);
            hotelImageView = (ImageView) itemView.findViewById(R.id.hotel_card_view_image);
        }
    }

    public TextView getHotelTileTextView() {
        return hotelTileTextView;
    }

    public ImageView getHotelImageView() {
        return hotelImageView;
    }
}
