package com.example.dhfls.testmikepenzandviewpager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dhfls.testmikepenzandviewpager.R;
import com.example.dhfls.testmikepenzandviewpager.cardview.AttractionRecyclerView.AttractionRecyclerViewDataAdapter;
import com.example.dhfls.testmikepenzandviewpager.cardview.AttractionRecyclerView.AttractionRecyclerViewItem;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// This Firstfragment is for Attraction RecyclerView.
public class FirstFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AttractionRecyclerViewDataAdapter attractionRecyclerViewDataAdapter;

    boolean isScrolling = false;
    boolean checkNULL = true;

    int currentItems, scrollOutItems, totalItemCount;

    static final int FIRST_FOUR_IMAGE = 4;
    static final int IMAGE_PER_PAGE = 4;
    int imagePerPage = 0;

    /**
     * It is for image and text in this fragment
     * It is the lists of image and text
     */
    List<AttractionRecyclerViewItem> attractionItemList = null;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
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

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Create the recyclerview object
        recyclerView = (RecyclerView) view.findViewById(R.id.first_fragment_card_view_recycler_list);

        initializeData();

        //testInitializeItemList();


        // Create the Linear layout manager
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        // Create the Grid layout manager with 2 columns. *** This is not used ***
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 1);

        // Set layout manager.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Create attraction recycler view data adapter with attraction item list
        attractionRecyclerViewDataAdapter = new AttractionRecyclerViewDataAdapter(attractionItemList);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        // Set data adapter
        recyclerView.setAdapter(attractionRecyclerViewDataAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

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

                    isScrolling = false;
                    recyclerView.stopScroll();
                    String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?" +
                            "ServiceKey=MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D&numOfRows=200"
                            + "&mapX=126.981611&mapY=37.568477&radius=700&listYN=Y&arrange=S"
                            + "&MobileOS=AND&MobileApp=appName&contentTypeId=12&_type=json";
                    String[] imageUrl = new String[100];
                    //Bitmap[] imageBitmap = new Bitmap[100];

                    InitializeAsyncTask initializeAsyncTask = new InitializeAsyncTask();
                    try {
                        imageUrl = initializeAsyncTask.execute(apiUrl).get();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    for(int i = imagePerPage; i < imagePerPage + IMAGE_PER_PAGE; i++){
                        if(imagePerPage == imageUrl.length) break;
                        attractionItemList.add(new AttractionRecyclerViewItem(title_list[i], imageUrl[i]));
                    }
                    attractionRecyclerViewDataAdapter.notifyDataSetChanged();
                    imagePerPage += IMAGE_PER_PAGE;
                }
            }
        });
    }
    public String title_list[] = new String[100];
    public class InitializeAsyncTask extends AsyncTask<String, Void, String[]>{

        String jsonResult = null;
        String image_list[] = new String[100];

        //Bitmap bitmap[] = new Bitmap[100];

        public int scrollCount=1;
        public int loadCount = 4;


        public Long contentId_list[] = new Long[200];

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
                //System.out.println("JSON 형식  :  " + xmlResult);

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
                    contentId_list[i]=contentID;


                    title_list[i]= title;
                    image_list[i] = photo;

                    Log.d("URL : ",image_list[i]);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return image_list;
        }
    }

    public void initializeData(){
        String apiUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?" +
                "ServiceKey=MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D&numOfRows=200"
                + "&mapX=126.981611&mapY=37.568477&radius=700&listYN=Y&arrange=S"
                + "&MobileOS=AND&MobileApp=appName&contentTypeId=12&_type=json";
        String[] imageUrl = new String[100];
        //Bitmap[] imageBitmap = new Bitmap[100];

        InitializeAsyncTask initializeAsyncTask = new InitializeAsyncTask();
        try {
            imageUrl = initializeAsyncTask.execute(apiUrl).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("imageBitmap Check", imageUrl.toString());

        // 이미지 뷰 초기화
        if(attractionItemList == null) {
            Log.d("Initialization", "initialize");
            attractionItemList = new ArrayList<AttractionRecyclerViewItem>();
        }
        // 첫 이미지 4개 등록
        for(int i = 0; i < FIRST_FOUR_IMAGE; i++){
            attractionItemList.add(new AttractionRecyclerViewItem(title_list[i], imageUrl[i]));
            imagePerPage = FIRST_FOUR_IMAGE;
        }
    }
}


