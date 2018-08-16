package com.example.dhfls.testmikepenzandviewpager.cardview.AttractionRecyclerView;

public class AttractionRecyclerViewItem {

    // Save cultural place's name
    private String attractionName;

    // Save image of the attraction
    private String attractionImageUrl;

    private Long contentID;


    public AttractionRecyclerViewItem(String attractionName, String attractionImageUrl, Long contentID){
        this.attractionName = attractionName;
        this.attractionImageUrl = attractionImageUrl;
        this.contentID = contentID;
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

    public Long getContentID() {
        return contentID;
    }

    public void setContentID(Long contentID) {
        this.contentID = contentID;
    }
}
