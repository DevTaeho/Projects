package com.example.dhfls.testmikepenzandviewpager.cardview.RestaurantRecyclerView;

public class RestaurantRecyclerViewItem {

    private String restaurantName;
    private String restaurantImageUrl;

    public RestaurantRecyclerViewItem(String restaurantName, String restaurantImageUrl) {
        this.restaurantName = restaurantName;
        this.restaurantImageUrl = restaurantImageUrl;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public void setRestaurantImageUrl(String restaurantImageUrl) {
        this.restaurantImageUrl = restaurantImageUrl;
    }
}
