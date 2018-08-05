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

public class AttractionInDetail extends AppCompatActivity {

    ViewPager viewPager;
    AttractionDetailViewPagerAdapter adapter;

    Toolbar toolbar;
    TextView toolbarTitleTextView;
    String toolbarTitle;
    String contentId;
    Intent getToolbarTitleIntent;

    String[] image_list = new String[20];

    private String[] images = {
            "https://www.gstatic.com/webp/gallery3/1.sm.png",
            "https://www.gstatic.com/webp/gallery/4.sm.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMtiv_1sQ1kIpuQrMvFLN5Tk12qM1OzOeK_GgeK5yICVvp-sF-jQ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_in_detail);

        // Link those views.
        toolbar = (Toolbar)findViewById(R.id.toolbarDetail);
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title_detailActivity);

        // Toolbar setting.
        getToolbarTitleIntent = getIntent();
        toolbarTitle = getToolbarTitleIntent.getExtras().getString("toolbarTitle");
        contentId = getToolbarTitleIntent.getExtras().getString("contentId");
        toolbarTitleTextView.setText(toolbarTitle);

        getDetailAsyncTask getDetailAsyncTask = new getDetailAsyncTask();
        try{
            image_list = getDetailAsyncTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }


        viewPager = (ViewPager) findViewById(R.id.attraction_detail_viewpager);
        adapter = new AttractionDetailViewPagerAdapter(this, image_list);
        viewPager.setAdapter(adapter);
    }

    public class getDetailAsyncTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... strings) {
            String[] imageList;
            imageList = APIParser_Array();
            return imageList;
        }
    }

    public String TOUR_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService";
    public String KEY = "MnY8VWII4nsP44khsrarNrTViOaLi14QbVuou%2BLadpJkKOEtdhkjSxI4dLfss2Abj7G1rifBKek1mvXG4YoplA%3D%3D";

    String jsonResult = null;


    /*String addr_list[] = new String[10];
    String detail_list[] = new String[10];
    String homepage[] = new String[10];
    String tel[] = new String[10];
    String title[] = new String[10];*/


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


    public String[] APIParser_Array(){

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
                image_list[i]=photo;
                Log.d("imageUrl", image_list[i]);
            }

            return image_list;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;
        }

    }


    public void APIParser() {

        //세부내용 파싱
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(getURLParam());
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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
}
