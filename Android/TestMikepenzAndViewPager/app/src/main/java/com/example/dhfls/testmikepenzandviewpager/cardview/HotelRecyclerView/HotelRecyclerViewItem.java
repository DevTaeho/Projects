package com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView;

public class HotelRecyclerViewItem {

    private String hotelName;
    private String hotelImageUrl;

    public HotelRecyclerViewItem(String hotelName, String hotelImageUrl) {
        this.hotelName = hotelName;
        this.hotelImageUrl = hotelImageUrl;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName){
        this.hotelName = hotelName;
    }

    public String getHotelImageUrl() {
        return hotelImageUrl;
    }

    public void setHotelImageUrl(String hotelImageUrl){
        this.hotelImageUrl = hotelImageUrl;
    }


}
