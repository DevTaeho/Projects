package com.example.dhfls.testmikepenzandviewpager.cardview.RestaurantRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dhfls.testmikepenzandviewpager.R;

public class RestaurantRecyclerViewItemHolder extends RecyclerView.ViewHolder {

    TextView restaurantTileTextView;
    ImageView restaurantImageView;

    public RestaurantRecyclerViewItemHolder(View itemView) {
        super(itemView);

        if(itemView != null){
            restaurantTileTextView = (TextView) itemView.findViewById(R.id.restaurant_card_view_image_title);
            restaurantImageView = (ImageView) itemView.findViewById(R.id.restaurant_card_view_image);
        }
    }

    public TextView getRestaurantTileTextView() {
        return restaurantTileTextView;
    }

    public ImageView getRestaurantImageView() {
        return restaurantImageView;
    }

}
