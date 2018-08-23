package com.example.dhfls.testmikepenzandviewpager.iconactivity.details;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dhfls.testmikepenzandviewpager.R;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AttractionInDetail extends AppCompatActivity {

    ViewPagerFixed viewPager;
    AttractionDetailViewPagerAdapter adapter;

    Toolbar toolbar;
    TextView toolbarTitleTextView;
    String toolbarTitle;
    String contentId;
    Intent getToolbarTitleIntent;

    TextView attractionDetailInfoTextView;


    String[] dynamicImageList;

    List<String> imageUrlList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_in_detail);

        // Link those views.
        toolbar = (Toolbar)findViewById(R.id.toolbarDetail);
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title_detailActivity);

        // Intent for getting data from another activities.
        getToolbarTitleIntent = getIntent();

        // Get Attraction name for the toolbar title.
        toolbarTitle = getToolbarTitleIntent.getExtras().getString("toolbarTitle");
        // Get content ID for parsing Image url.
        contentId = getToolbarTitleIntent.getExtras().getString("contentId");
        toolbarTitleTextView.setText(toolbarTitle);

        // Get TextView that displays the information of attraction in detail.
        attractionDetailInfoTextView = (TextView) findViewById(R.id.attraction_detail_viewpager_textview);
        // Get detail Data of Attraction by calling 'attractionDetailInfo()' method.
        String detailData = attractionDetailInfo();
        attractionDetailInfoTextView.setText(detailData);

        Log.d("ContentID", contentId);

        getDetailAsyncTask getDetailAsyncTask = new getDetailAsyncTask();
        try{
            getDetailAsyncTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        dynamicImageList = makeDynamicImageUrl(imageUrlList);

        // Find ViewPager and set adapter with AttractionDetailViewPagerAdapter.
        viewPager = (ViewPagerFixed) findViewById(R.id.attraction_detail_viewpager);
        adapter = new AttractionDetailViewPagerAdapter(this, dynamicImageList);
        viewPager.setAdapter(adapter);
    }

    public class getDetailAsyncTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            APIParser_Array();
            return null;
        }
    }

    public String TOUR_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService";
    public String KEY = "MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D";

    String jsonResult = null;


    public String getJSON(String res) {
        try {
            URL url = new URL(res);
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
            return jsonResult;

        } catch (IOException e) {
                e.printStackTrace();
                return null;
        }
    }


    public void APIParser_Array(){

        try {

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(getJSON(getURLParam2()));
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");

            JSONArray item = (JSONArray) items.get("item");


            for(int i=0;i<item.size();i++) {
                //이미지 리스트 파싱
                JSONObject list = (JSONObject) item.get(i);
                String photo = list.get("originimgurl").toString();
                imageUrlList.add(photo);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }


    public String attractionDetailInfo() {

        //세부내용 파싱
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(getJSON(getURLParam()));
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");

            JSONObject item = (JSONObject) items.get("item");

            String photo = (String) item.get("firstimage"); //이미지
            String addr = (String) item.get("addr1");  //주소
            String title = (String) item.get("title"); //이름
            String detail = (String) item.get("overview");  //상세정보
            String homepage = (String) item.get("homepage"); //홈페이지 주소
            String tel = (String) item.get("tel"); //전화번호

            detail = detail.replace("<br>","\n");

            return detail;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("detail info Err", e.toString());
            return "Failed to read data";
        }


    }

    private String getURLParam() {

        String apiUrl = TOUR_URL + "/detailCommon?ServiceKey=" + KEY +
                "&contentId=" + contentId + "&defaultYN=Y&firstImageYN=Y&addrinfoYN=Y&overviewYN=Y" +
                "&MobileOS=AND&MobileApp=appName&_type=json";
        return apiUrl;
    }

    private String getURLParam2() {

        String apiUrl = TOUR_URL + "/detailImage?ServiceKey=" + KEY +
                "&contentId=" + contentId + "&imageYN=Y" +
                "&MobileOS=AND&MobileApp=appName&_type=json";
        return apiUrl;
    }

    /**
     *
     * @param imageUrlList
     * @return
     *
     * Allocate the data in imageUrlList to imageList String array for the number of images it has.
     * So, Consequently Viewpager can display images for the number of images the imageUrlList has.
     */
    private String[] makeDynamicImageUrl(List<String> imageUrlList){

        String[] imageList = new String[imageUrlList.size()];

        for(int i = 0; i<imageUrlList.size(); i++){
            imageList[i] = imageUrlList.get(i);
        }
        return imageList;
    }
}
