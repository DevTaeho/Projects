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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.dhfls.testmikepenzandviewpager.R;
import com.example.dhfls.testmikepenzandviewpager.cardview.RestaurantRecyclerView.RestaurantRecyclerViewDataAdapter;
import com.example.dhfls.testmikepenzandviewpager.cardview.RestaurantRecyclerView.RestaurantRecyclerViewItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This Fragment is for showing dining room.
 * Like FirstFragment showing those food shops and People can save the store that they have great experience with.
 */

public class ThirdFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    RecyclerView restaurantRecyclerView;

    LinearLayoutManager linearLayoutManager;

    RestaurantRecyclerViewDataAdapter restaurantRecyclerViewDataAdapter;

    List<RestaurantRecyclerViewItem> restaurantItemList = null;

    int currentItems, scrollOutItems, totalItemCount;

    static final int FIRST_FOUR_IMAGE = 4;
    static final int IMAGE_PER_PAGE = 4;
    int imagePerPage = 0;

    // newInstance constructor for creating fragment with arguments
    public static ThirdFragment newInstance(int page, String title) {
        ThirdFragment fragmentThird = new ThirdFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentThird.setArguments(args);
        return fragmentThird;
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
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        restaurantRecyclerView = (RecyclerView) view.findViewById(R.id.restaurant_fragment_card_view_recycler_list);

        initializeData();

        linearLayoutManager = new LinearLayoutManager(this.getContext());

        restaurantRecyclerView.setLayoutManager(linearLayoutManager);
        restaurantRecyclerView.setHasFixedSize(true);

        restaurantRecyclerViewDataAdapter = new RestaurantRecyclerViewDataAdapter(restaurantItemList);

        restaurantRecyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);

        restaurantRecyclerView.setAdapter(restaurantRecyclerViewDataAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            + "&MobileOS=AND&MobileApp=appName&contentTypeId=39&_type=json";
                    String[] imageUrl = new String[100];

                    InitializeRestaurantAsyncTask initializeAsyncTask = new InitializeRestaurantAsyncTask();
                    try {
                        imageUrl = initializeAsyncTask.execute(apiUrl).get();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    for(int i = imagePerPage; i < imagePerPage + IMAGE_PER_PAGE; i++){
                        if(imagePerPage == imageUrl.length) break;
                        restaurantItemList.add(new RestaurantRecyclerViewItem(restaurant_title_list[i], imageUrl[i]));
                    }
                    restaurantRecyclerViewDataAdapter.notifyDataSetChanged();
                    imagePerPage += IMAGE_PER_PAGE;
                }
            }
        });
    }

    private String restaurant_title_list[] = new String[100];
    private Long restaurant_contentID_list[] = new Long[100];
    public class InitializeRestaurantAsyncTask extends AsyncTask<String, Void, String[]> {

        String jsonResult = null;
        String restaurant_image_list[] = new String[100];

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

                    restaurant_contentID_list[i]=contentID;
                    restaurant_title_list[i]= title;
                    restaurant_image_list[i] = photo;

                    Log.d("URL : ",restaurant_image_list[i]);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return restaurant_image_list;
        }
    }

    public void initializeData(){
        String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?" +
                "ServiceKey=MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D&numOfRows=200"
                + "&mapX=126.981611&mapY=37.568477&radius=700&listYN=Y&arrange=S"
                + "&MobileOS=AND&MobileApp=appName&contentTypeId=39&_type=json";
        String[] restaurantImageUrl = new String[100];

        InitializeRestaurantAsyncTask initializeAsyncTask = new InitializeRestaurantAsyncTask();
        try {
            restaurantImageUrl = initializeAsyncTask.execute(apiUrl).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("imageBitmap Check", restaurantImageUrl.toString());

        // 이미지 뷰 초기화
        if(restaurantItemList == null) {
            Log.d("Initialization", "initialize");
            restaurantItemList = new ArrayList<RestaurantRecyclerViewItem>();
        }
        // 첫 이미지 4개 등록
        for(int i = 0; i < FIRST_FOUR_IMAGE; i++){
            Log.d("First 4 images",restaurantImageUrl[i]);
            restaurantItemList.add(new RestaurantRecyclerViewItem(restaurant_title_list[i], restaurantImageUrl[i]));
            imagePerPage = FIRST_FOUR_IMAGE;
        }
    }
}