package com.example.com.myapplication12;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by com on 2017-04-09.
 */

public class SplashActivity extends Activity {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(!nfcAdapter.isNdefPushEnabled()){
            Toast.makeText(this, "NFC 읽기/쓰기 모드로 활성화해주세요", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "어서오세요", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }
}
