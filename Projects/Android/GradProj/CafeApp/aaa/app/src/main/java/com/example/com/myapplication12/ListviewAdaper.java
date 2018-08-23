package com.example.com.myapplication12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by com on 2017-04-08.
 */

public class ListviewAdaper extends ArrayAdapter {

    private LayoutInflater inflater;
    private ArrayList<Listviewitem> data = null;
    private int layout;

    public ListviewAdaper(Context context, int layout, ArrayList<Listviewitem> data){
        super(context, layout, data);
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount(){return data.size();}

    @Override
    public String getItem(int position){return data.get(position).getName();}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView=inflater.inflate(layout,parent,false);
        }

        Listviewitem listviewitem = data.get(position);

        ImageView bitmap = (ImageView)convertView.findViewById(R.id.imageView);
        bitmap.setImageBitmap(listviewitem.getBitmap());

        TextView name = (TextView)convertView.findViewById(R.id.textview);
        name.setText(listviewitem.getName());

        TextView price = (TextView)convertView.findViewById(R.id.textview2);
        price.setText(listviewitem.getPrice());

        return convertView;
    }
}
