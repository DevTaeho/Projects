package com.example.dhfls.testmikepenzandviewpager.loginandsession;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dhfls.testmikepenzandviewpager.MainActivity;
import com.example.dhfls.testmikepenzandviewpager.R;

public class FirstAuthActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(SessionControl.getUserName(FirstAuthActivity.this).length() == 0 || SessionControl.getUserPW(FirstAuthActivity.this).length() == 0){
            Toast.makeText(this,"정보 없음",Toast.LENGTH_SHORT).show();
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(this,"정보 있음",Toast.LENGTH_SHORT).show();
            intent = new Intent(FirstAuthActivity.this,MainActivity.class);
            intent.putExtra("STD_NUM",SessionControl.getUserName(this).toString());
            startActivity(intent);
            this.finish();
        }
    }
}
