package com.example.com.myapplication12;

import android.graphics.Bitmap;

/**
 * Created by com on 2017-04-08.
 */

public class Listviewitem {

    private String name;
    private String price;
    private Bitmap bitmap;

    public String getName() {return  name;}
    public String getPrice() {return price;}
    public Bitmap getBitmap() {return bitmap;}

    public Listviewitem(String name,String price, Bitmap bitmap){
        this.name=name;
        this.price=price;
        this.bitmap=bitmap;
    }
}
