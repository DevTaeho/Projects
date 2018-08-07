package com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView;

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

public class HotelRecyclerViewDataAdapter extends RecyclerView.Adapter<HotelRecyclerViewItemHolder> {

    private List<HotelRecyclerViewItem> hotelItemList;

    static final int NUM_HOTEL_IMAGES = 150;


    public HotelRecyclerViewDataAdapter(List<HotelRecyclerViewItem> hotelItemList) {
        this.hotelItemList = hotelItemList;
    }

    @NonNull
    @Override
    public HotelRecyclerViewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        final View hotelItemView = layoutInflater.inflate(R.layout.hotelfragment_card_view_item, parent, false);

        final TextView hotelTitleTextView = (TextView) hotelItemView.findViewById(R.id.hotel_card_view_image_title);
        final ImageView hotelImageView = (ImageView) hotelItemView.findViewById(R.id.hotel_card_view_image);

        final ImageView hotelCardViewFavoriteIcon = (ImageView) hotelItemView.findViewById(R.id.hotel_card_view_icon_favorite);
        final ImageView hotelCardViewMoreInfoIcon = (ImageView) hotelItemView.findViewById(R.id.hotel_card_view_icon_moreInfo);
        final ImageView hotelCardViewLocationIcon = (ImageView) hotelItemView.findViewById(R.id.hotel_card_view_icon_location);
        final ImageView hotelCardViewShareIcon = (ImageView) hotelItemView.findViewById(R.id.hotel_card_view_icon_group);

        HotelRecyclerViewItemHolder hotelRecyclerViewItemHolder = new HotelRecyclerViewItemHolder(hotelItemView);

        return hotelRecyclerViewItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelRecyclerViewItemHolder holder, int position) {

        HotelRecyclerViewItem hotelRecyclerViewItem = hotelItemList.get(position);

        if(hotelRecyclerViewItem != null){
            holder.getHotelTileTextView().setText(hotelRecyclerViewItem.getHotelName());
            Glide
                    .with(holder.getHotelImageView().getContext())
                    .load(hotelRecyclerViewItem.getHotelImageUrl())
                    .into(holder.hotelImageView);
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(hotelItemList != null){
            ret = hotelItemList.size();
        }
        return ret;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
