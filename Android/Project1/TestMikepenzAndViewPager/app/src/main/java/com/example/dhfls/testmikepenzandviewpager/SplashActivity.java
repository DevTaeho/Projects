package com.example.dhfls.testmikepenzandviewpager;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dhfls.testmikepenzandviewpager.loginandsession.FirstAuthActivity;
import com.example.dhfls.testmikepenzandviewpager.loginandsession.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private final int DELAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, FirstAuthActivity.class);
                SplashActivity.this.startActivity(intent);

                finish();
            }
        }, DELAY_TIME);
    }




}
