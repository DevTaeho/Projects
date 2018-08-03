package com.example.dhfls.testmikepenzandviewpager.cardview.AttractionRecyclerView;

public class AttractionRecyclerViewItem {

    // Save cultural place's name
    private String attractionName;

    // Save image of the attraction
    private String attractionImageUrl;

    public AttractionRecyclerViewItem(String attractionName, String attractionImageUrl){
        this.attractionName = attractionName;
        this.attractionImageUrl = attractionImageUrl;
    }

    public String getAttractionName(){
        return attractionName;
    }

    public void setAttractionName(String attractionName){
        this.attractionName = attractionName;
    }

    public String getAttractionImageUrl(){
        return attractionImageUrl;
    }

    public void setAttractionImageUrl(String  attractionImageUrl) {
        this.attractionImageUrl = attractionImageUrl;
    }
}
