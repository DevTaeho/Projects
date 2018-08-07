package com.example.dhfls.testmikepenzandviewpager.loginandsession;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dhfls.testmikepenzandviewpager.MainActivity;
import com.example.dhfls.testmikepenzandviewpager.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    boolean doubleBackToExtiPressedOnce = false;

    AppCompatButton loginButton;

    EditText inputID, inputPW;
    TextView signupText;
    HttpPost httpPost,httpPost1;
    HttpResponse response,response1;
    HttpClient httpclient,httpclient1;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    @Override
    public void onBackPressed() {
        if(doubleBackToExtiPressedOnce){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        inputID = (EditText) findViewById(R.id.input_email);
        inputPW = (EditText) findViewById(R.id.input_password);

        signupText = (TextView) findViewById(R.id.link_signup);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        loginButton = (AppCompatButton) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Validating user...", true);

                new Thread(new Runnable() {
                    public void run() {
                        login();
                    }
                }).start();
            }
        });

    }

    void login(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        /*
        try{
            String userID = inputID.getText().toString().trim();
            String userPW = inputPW.getText().toString().trim();

            httpclient = SessionControl.getHttpclient();
            httpPost = new HttpPost("http://192.168.0.3/login.php");
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", userID));
            nameValuePairs.add(new BasicNameValuePair("password", userPW));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httpPost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httpPost,responseHandler);
            System.out.println("Response : " + response);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this,"Response from PHP : " + response, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });

            if(response.contains("success")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SessionControl.cookies = SessionControl.httpClient.getCookieStore().getCookies();
                    }
                });

                httpclient1 = SessionControl.getHttpclient();
                httpPost1 = new HttpPost("http://192.168.0.3/nameget.php");
                httpPost1.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response1 = httpclient1.execute(httpPost1);
                ResponseHandler<String> responseHandler1 = new BasicResponseHandler();
                final String response1 = httpclient1.execute(httpPost1,responseHandler1);

                SessionControl.setUserName(LoginActivity.this,response1);
                SessionControl.setUserID(LoginActivity.this,inputID.getText().toString());
                SessionControl.setUserPW(LoginActivity.this,inputPW.getText().toString());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }*/
    }
}
