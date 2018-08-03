package com.example.dhfls.testmikepenzandviewpager.cardview.AttractionRecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dhfls.testmikepenzandviewpager.R;
import com.example.dhfls.testmikepenzandviewpager.iconactivity.details.AttractionGroup;
import com.example.dhfls.testmikepenzandviewpager.iconactivity.details.AttractionInDetail;
import com.example.dhfls.testmikepenzandviewpager.iconactivity.details.AttractionLocation;

import java.util.List;

public class AttractionRecyclerViewDataAdapter extends RecyclerView.Adapter<AttractionRecyclerViewItemHolder> {

    private List<AttractionRecyclerViewItem> attractionItemList;
    public static boolean favoriteCheck;
    private int favoriteCheck2 = 0;

    static final int NUM_ATTRACTION_IMAGES = 150;

    String[] imageArray = new String[NUM_ATTRACTION_IMAGES];
    String imageUrl;

    public AttractionRecyclerViewDataAdapter(List<AttractionRecyclerViewItem> attractionItemList){
        this.attractionItemList = attractionItemList;
    }

    @NonNull
    @Override
    public AttractionRecyclerViewItemHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        // Get LayoutInflater object
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the Recylcerview item layout xml
        final View attractionItemView = layoutInflater.inflate(R.layout.fragment_card_view_item, parent, false);


        // Get attraction title text and image view object.
        final TextView attractionTitleView = (TextView) attractionItemView.findViewById(R.id.card_view_image_title);

        // Get attraction image view object
        final ImageView attractionImageView = (ImageView) attractionItemView.findViewById(R.id.card_view_image);

        // Get Icons view object.
        final ImageView cardViewFavoriteIcon = (ImageView) attractionItemView.findViewById(R.id.card_view_icon_favorite);
        final ImageView cardViewMoreInfoIcon = (ImageView) attractionItemView.findViewById(R.id.card_view_icon_moreInfo);
        final ImageView cardViewPartyIcon = (ImageView) attractionItemView.findViewById(R.id.card_view_icon_group);
        final ImageView cardVIewLocationIcon = (ImageView) attractionItemView.findViewById(R.id.card_view_icon_location);


        // onClickListener for favorite icon.
        cardViewFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++favoriteCheck2;
                if(favoriteCheck2 % 2 == 1){
                    favoriteCheck = true;
                    cardViewFavoriteIcon.setImageResource(R.drawable.mark_as_favorite_on);
                }else{
                    favoriteCheck = false;
                    cardViewFavoriteIcon.setImageResource(R.drawable.mark_as_favorite_off);
                }
            }
        });

        // onClickListener for MoreInfo icon.
        cardViewMoreInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get attraction title for throwing title to icon activity.
                String attractionTitle = attractionTitleView.getText().toString();

                Intent intent = new Intent(view.getContext(), AttractionInDetail.class);

                intent.putExtra("toolbarTitle", attractionTitle);
                view.getContext().startActivity(intent);
            }
        });

        // onClickListener for group icon.
        cardViewPartyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get attraction title for throwing title to icon activity.
                String attractionTitle = attractionTitleView.getText().toString();

                int position = viewType;

                imageUrl = imageArray[position];

                Log.d("view position :", " "+position);
                Intent intent = new Intent(view.getContext(), AttractionGroup.class);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("toolbarTitle", attractionTitle);
                view.getContext().startActivity(intent);
            }
        });

        // onClickListener for location icon.
        cardVIewLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get attraction title for throwing title to icon activity.
                String attractionTitle = attractionTitleView.getText().toString();
                Intent intent = new Intent(view.getContext(), AttractionLocation.class);
                intent.putExtra("toolbarTitle", attractionTitle);
                view.getContext().startActivity(intent);
            }
        });

        // Create and Return our customized Recycler View Item Holder object
        AttractionRecyclerViewItemHolder ret = new AttractionRecyclerViewItemHolder(attractionItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionRecyclerViewItemHolder holder, int position) {
        // Get attraction item dto in list
        AttractionRecyclerViewItem attractionItem = attractionItemList.get(position);

        holder.getAttractionImageView().setVisibility(View.VISIBLE);

        if(attractionItem != null){
            // Set attraction item title
            holder.getAttractionTitleText().setText(attractionItem.getAttractionName());
            // Set attraction item image
            imageArray[position] = attractionItem.getAttractionImageUrl();
            Glide
                    .with(holder.getAttractionImageView().getContext())
                    .load(attractionItem.getAttractionImageUrl())
                    .into(holder.getAttractionImageView());
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(attractionItemList != null){
            ret = attractionItemList.size();
        }
        return ret;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
