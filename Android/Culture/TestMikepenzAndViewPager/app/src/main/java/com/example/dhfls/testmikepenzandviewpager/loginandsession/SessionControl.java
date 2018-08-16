package com.example.dhfls.testmikepenzandviewpager.loginandsession;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

public class SessionControl {
    static public DefaultHttpClient httpClient = null;
    static public List<Cookie> cookies;

    public static HttpClient getHttpclient(){
        if(httpClient == null){
            SessionControl.setHttpClient(new DefaultHttpClient());
        }
        return httpClient;
    }

    public static void setHttpClient(DefaultHttpClient httpClient){
        SessionControl.httpClient = httpClient;
    }

    static final String PREF_USER_ID = "userID";
    static final String PREF_USER_PW = "userPW";
    static final String PREF_USER_NAME = "username";

    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    //계정정보 저장
    public static void setUserID(Context ctx, String userID){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.commit();
    }
    public static void setUserPW(Context ctx, String userPW){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PW, userPW);
        editor.commit();
    }
    public static void setUserName(Context ctx, String userName){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME,userName);
        editor.commit();
    }
    //저장정보 가져오기
    public static String getUserID(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_ID,"");
    }
    public static String getUserPW(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_PW,"");
    }
    public static String getUserName(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_NAME,"");
    }
    //로그아웃
    public static void clearUserID(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
