package com.example.dhfls.testmikepenzandviewpager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dhfls.testmikepenzandviewpager.R;
import com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView.HotelRecyclerViewDataAdapter;
import com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView.HotelRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    RecyclerView hotelRecyclerView;

    RecyclerView.LayoutManager layoutManager;

    LinearLayoutManager linearLayoutManager;

    HotelRecyclerViewDataAdapter hotelRecyclerViewDataAdapter;

    List<HotelRecyclerViewItem> hotelItemList = null;

    // newInstance constructor for creating fragment with arguments
    public static SecondFragment newInstance(int page, String title) {
        SecondFragment fragmentSecond = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentSecond.setArguments(args);
        return fragmentSecond;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        hotelRecyclerView = (RecyclerView) view.findViewById(R.id.hotel_fragment_card_view_recycler_list);

        hotelItemList = new ArrayList<HotelRecyclerViewItem>();
        hotelItemList.add(new HotelRecyclerViewItem("hi1","https://www.gstatic.com/webp/gallery3/1.sm.png"));
        hotelItemList.add(new HotelRecyclerViewItem("hi2","https://www.gstatic.com/webp/gallery/4.sm.jpg"));
        hotelItemList.add(new HotelRecyclerViewItem("hi3","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMtiv_1sQ1kIpuQrMvFLN5Tk12qM1OzOeK_GgeK5yICVvp-sF-jQ"));

        layoutManager = new LinearLayoutManager(this.getContext());

        hotelRecyclerView.setLayoutManager(layoutManager);
        hotelRecyclerView.setHasFixedSize(true);

        hotelRecyclerViewDataAdapter = new HotelRecyclerViewDataAdapter(hotelItemList);

        hotelRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        // Set data adapter
        hotelRecyclerView.setAdapter(hotelRecyclerViewDataAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hotelRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public class getImageUrlAsyncTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... url) {

            return null;
        }
    }
}