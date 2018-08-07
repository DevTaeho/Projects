package com.example.dhfls.testmikepenzandviewpager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.dhfls.testmikepenzandviewpager.R;
import com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView.HotelRecyclerViewDataAdapter;
import com.example.dhfls.testmikepenzandviewpager.cardview.HotelRecyclerView.HotelRecyclerViewItem;



public class SecondFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    RecyclerView hotelRecyclerView;

    LinearLayoutManager linearLayoutManager;

    HotelRecyclerViewDataAdapter hotelRecyclerViewDataAdapter;

    List<HotelRecyclerViewItem> hotelItemList = null;

    int currentItems, scrollOutItems, totalItemCount;

    static final int FIRST_FOUR_IMAGE = 4;
    static final int IMAGE_PER_PAGE = 4;
    int imagePerPage = 0;

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

        initializeData();

        // Create the Linear layout manager
        linearLayoutManager = new LinearLayoutManager(this.getContext());

        hotelRecyclerView.setLayoutManager(linearLayoutManager);
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

                // Currently visible Items
                currentItems = linearLayoutManager.getChildCount();
                // Total items in adapter
                totalItemCount = linearLayoutManager.getItemCount();
                // Scrolled out of screen
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(currentItems + scrollOutItems == totalItemCount){
                    //data fetch

                    recyclerView.stopScroll();
                    String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?" +
                            "ServiceKey=MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D&numOfRows=200"
                            + "&mapX=126.981611&mapY=37.568477&radius=700&listYN=Y&arrange=S"
                            + "&MobileOS=AND&MobileApp=appName&contentTypeId=32&_type=json";
                    String[] imageUrl = new String[100];

                    InitializeHotelAsyncTask initializeAsyncTask = new InitializeHotelAsyncTask();
                    try {
                        imageUrl = initializeAsyncTask.execute(apiUrl).get();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    for(int i = imagePerPage; i < imagePerPage + IMAGE_PER_PAGE; i++){
                        if(imagePerPage == imageUrl.length) break;
                        hotelItemList.add(new HotelRecyclerViewItem(hotel_title_list[i], imageUrl[i]));
                    }
                    hotelRecyclerViewDataAdapter.notifyDataSetChanged();
                    imagePerPage += IMAGE_PER_PAGE;
                }

            }
        });
    }

    private String hotel_title_list[] = new String[100];
    private Long hotel_contentID_list[] = new Long[100];
    public class InitializeHotelAsyncTask extends AsyncTask<String, Void, String[]>{

        String jsonResult = null;
        String hotel_image_list[] = new String[100];

        @Override
        protected String[] doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                StringBuffer buffer = new StringBuffer();
                String line = null;
                String tmpStr = null;

                while ((line = reader.readLine()) != null) {
                    tmpStr = line.toString();
                    tmpStr = tmpStr.replaceAll(" ", "");
                    if (!tmpStr.equals("")) {
                        buffer.append(line).append("\r\n");
                    }
                }

                reader.close();
                jsonResult = buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonResult);
                JSONObject StockInfoArray = (JSONObject) jsonObject.get("response");
                JSONObject StockInfoArray2 = (JSONObject) StockInfoArray.get("body");
                JSONObject items = (JSONObject) StockInfoArray2.get("items");

                JSONArray item = (JSONArray) items.get("item");




                for (int i = imagePerPage; i < imagePerPage + IMAGE_PER_PAGE; i++) {
                    JSONObject list = (JSONObject) item.get(i);
                    String photo = (String) list.get("firstimage");
                    String title = (String) list.get("title");
                    Long contentID = (Long) list.get("contentid");

                    hotel_contentID_list[i]=contentID;
                    hotel_title_list[i]= title;
                    hotel_image_list[i] = photo;

                    Log.d("URL : ",hotel_image_list[i]);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return hotel_image_list;
        }
    }

    public void initializeData(){
        String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?" +
                "ServiceKey=MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D&numOfRows=200"
                + "&mapX=126.981611&mapY=37.568477&radius=700&listYN=Y&arrange=S"
                + "&MobileOS=AND&MobileApp=appName&contentTypeId=32&_type=json";
        String[] hotelImageUrl = new String[100];
        //Bitmap[] imageBitmap = new Bitmap[100];

        InitializeHotelAsyncTask initializeAsyncTask = new InitializeHotelAsyncTask();
        try {
            hotelImageUrl = initializeAsyncTask.execute(apiUrl).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("imageBitmap Check", hotelImageUrl.toString());

        // 이미지 뷰 초기화
        if(hotelItemList == null) {
            Log.d("Initialization", "initialize");
            hotelItemList = new ArrayList<HotelRecyclerViewItem>();
        }
        // 첫 이미지 4개 등록
        for(int i = 0; i < FIRST_FOUR_IMAGE; i++){
            Log.d("First 4 images",hotelImageUrl[i]);
            hotelItemList.add(new HotelRecyclerViewItem(hotel_title_list[i], hotelImageUrl[i]));
            imagePerPage = FIRST_FOUR_IMAGE;
        }
    }

}