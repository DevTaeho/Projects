package com.example.dhfls.testmikepenzandviewpager.cardview.AttractionRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dhfls.testmikepenzandviewpager.R;

public class AttractionRecyclerViewItemHolder extends RecyclerView.ViewHolder {

    private TextView attractionTitleText = null;
    private ImageView attractionImageView = null;
    private TextView attractionContentIdView = null;

    public AttractionRecyclerViewItemHolder(View itemView){
        super(itemView);

        if(itemView != null){
            attractionTitleText = (TextView)itemView.findViewById(R.id.card_view_image_title);
            attractionImageView = (ImageView)itemView.findViewById(R.id.card_view_image);
            attractionContentIdView = (TextView)itemView.findViewById(R.id.card_view_contentid);
        }
    }

    public TextView getAttractionTitleText() {
        return attractionTitleText;
    }

    public ImageView getAttractionImageView() {
        return attractionImageView;
    }

    public TextView getAttractionContentIdView() {
        return attractionContentIdView;
    }
}
