package com.example.dhfls.testmikepenzandviewpager.cardview.RestaurantRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dhfls.testmikepenzandviewpager.R;

import java.util.List;

public class RestaurantRecyclerViewDataAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewItemHolder> {

    private List<RestaurantRecyclerViewItem> restaurantItemList;

    public RestaurantRecyclerViewDataAdapter(List<RestaurantRecyclerViewItem> restaurantItemList) {
        this.restaurantItemList = restaurantItemList;
    }

    @NonNull
    @Override
    public RestaurantRecyclerViewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        final View restaurantItemView = layoutInflater.inflate(R.layout.restaurantfragment_card_view_item, parent, false);

        final TextView restaurantTextView = (TextView) restaurantItemView.findViewById(R.id.restaurant_card_view_image_title);
        final ImageView restaurantImageVIew = (ImageView) restaurantItemView.findViewById(R.id.restaurant_card_view_image);

        RestaurantRecyclerViewItemHolder restaurantRecyclerViewItemHolder = new RestaurantRecyclerViewItemHolder(restaurantItemView);

        return restaurantRecyclerViewItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerViewItemHolder holder, int position) {
        RestaurantRecyclerViewItem restaurantRecyclerViewItem = restaurantItemList.get(position);

        if(restaurantRecyclerViewItem != null){
            holder.getRestaurantTileTextView().setText(restaurantRecyclerViewItem.getRestaurantName());
            Glide
                    .with(holder.getRestaurantImageView().getContext())
                    .load(restaurantRecyclerViewItem.getRestaurantImageUrl())
                    .into(holder.restaurantImageView);
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(restaurantItemList != null){
            ret = restaurantItemList.size();
        }
        return ret;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
