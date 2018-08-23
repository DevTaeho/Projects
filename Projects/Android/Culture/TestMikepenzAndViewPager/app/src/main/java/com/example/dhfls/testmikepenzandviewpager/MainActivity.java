package com.example.dhfls.testmikepenzandviewpager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dhfls.testmikepenzandviewpager.fragment.FirstFragment;
import com.example.dhfls.testmikepenzandviewpager.fragment.SecondFragment;
import com.example.dhfls.testmikepenzandviewpager.fragment.ThirdFragment;
import com.example.dhfls.testmikepenzandviewpager.fragment.FourthFragment;
import com.example.dhfls.testmikepenzandviewpager.loginandsession.LoginActivity;
import com.example.dhfls.testmikepenzandviewpager.loginandsession.SessionControl;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity{

    TabLayout tabLayout;
    Toolbar toolbar;
    SearchView searchView;
    AccountHeader accountHeader;
    int tabIndex;
    FragmentPagerAdapter fragmentPagerAdapter;

    PrimaryDrawerItem item1, item2, item3, item4, item5;

    boolean doubleBackToExtiPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(doubleBackToExtiPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExtiPressedOnce = true;

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custumtoast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        Toast toast = new Toast(getApplicationContext());
        TextView textView = (TextView) layout.findViewById(R.id.toast_text);
        textView.setText("한번 더 클릭하면 종료됩니다");

        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExtiPressedOnce = false;
            }
        }, 2000);
    }

    // Main Activity "onCreate"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbarMain);

        /**
         * Inflate the searchView on toolbar.
         */
        toolbar.inflateMenu(R.menu.menu);
        searchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        /**
         * The below is about Mikepenz's Drawer Design.
         */
        //PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withIcon(R.drawable.ic_testpage111).withName("Home");
        item1 = setPrimaryItem(R.drawable.ic_drawer_settings, "설정");
        item2 = setPrimaryItem(R.drawable.ic_drawer_logout, "로그아웃");
        item3 = setPrimaryItem(R.drawable.ic_drawer_invite, "초대하기");
        item4 = setPrimaryItem(R.drawable.ic_drawer_evaluation, "이 앱을 평가해주세요");
        item5 = setPrimaryItem(R.drawable.ic_drawer_feedback, "피드백이 있으신가요?");

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(getResources().getDrawable(R.drawable.header))
                .addProfiles(
                        new ProfileDrawerItem().withName(SessionControl.getUserName(this)).withEmail(SessionControl.getUserID(this)).withIcon(getResources().getDrawable(R.drawable.profile2))
                )
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        item1, item2,  item3, item4, item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position){
                            case 1: break;
                            case 2: SessionControl.clearUserID(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 3:
                                onlinkButtonClicked();
                                break;
                            case 4: break;
                            case 5: break;
                            case 6: break;
                        }
                        return true;
                    }
                })
                .build();

        /**
         * This is the source code for view pager.
         */
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);
        fragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        /**
         * This is source code that inflate the icon into tabs.
         * You can not use 'tabIndex' variable.
         * I don't know the reason. I need to know the reason..
         */
        for(tabIndex=0; tabIndex < tabLayout.getTabCount(); tabIndex++){
            int image;
            int realTab;
            switch (tabIndex) {
                case 0:
                    realTab = 0;
                    image = R.drawable.ic_fragment_attraction;
                    tabLayout.getTabAt(realTab).setIcon(image);
                case 1:
                    realTab = 1;
                    image = R.drawable.ic_fragment_hotel;
                    tabLayout.getTabAt(realTab).setIcon(image);
                case 2:
                    realTab = 2;
                    image = R.drawable.ic_fragment_dining;
                    tabLayout.getTabAt(realTab).setIcon(image);
                case 3:
                    realTab = 3;
                    image = R.drawable.ic_testpage333;
                    tabLayout.getTabAt(realTab).setIcon(image);
            }
        }
    }
    //Main Activity onCreate END

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    // Fragment # 0 - This will show FirstFragment
                    return FirstFragment.newInstance(0, "Page # 1");
                case 1:
                    // Fragment # 1 - This will show SecondFragment
                    return SecondFragment.newInstance(1, "Page # 2");
                case 2:
                    // Fragment # 2 - This will show ThirdFragment
                    return ThirdFragment.newInstance(2, "Page # 3");
                case 3:
                    // Fragment # 3 - This will show ThirdFragment
                    return FourthFragment.newInstance(3, "Page # 4");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return null;
            }
        }

        private PrimaryDrawerItem setPrimaryItem(int icon, String name){
            PrimaryDrawerItem item = new PrimaryDrawerItem().withIcon(icon).withName(name);

            return item;
        }
    private void onlinkButtonClicked(){
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("Feelture",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder().setMobileWebUrl("https://developers.kakao.com").build()).build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();
// 현재 에러가 뜨는데 이유로 추정되는것 and 답변 https://devtalk.kakao.com/t/v2/53050에 있음

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });
    }
}
