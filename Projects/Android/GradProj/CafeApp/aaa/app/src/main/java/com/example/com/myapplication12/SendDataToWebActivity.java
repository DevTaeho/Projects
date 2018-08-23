package com.example.com.myapplication12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SendDataToWebActivity extends AppCompatActivity {

    private ArrayList<String> teleData;

    private String[] cofName = new String[10];
    private int[] cofPrice = new int[10];
    private int[] cofQuan = new int[10];

    private String cofNameParam="null";
    private String cofQuanParam;
    private String locationParam="null";

    private int cofNameIndex = 0;
    private int cofPriceIndex = 0;
    private int cofQuanIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data_to_web);
        Button button = (Button)findViewById(R.id.button_main_insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute();
                Toast.makeText(SendDataToWebActivity.this, "결제되었습니다", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SendDataToWebActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent1 = getIntent();
        teleData = intent1.getStringArrayListExtra("teleData");

        for(int i=0; i<teleData.size(); i++){
            if((i%3)==0){
                cofName[cofNameIndex] = teleData.get(i);
                cofNameIndex++;
            }else if((i%3)==1){
                cofPrice[cofPriceIndex] = Integer.parseInt(teleData.get(i));
                cofPriceIndex++;
            }else if((i%3)==2){
                cofQuan[cofQuanIndex] = Integer.parseInt(teleData.get(i));
                cofQuanIndex++;
            }
        }

        for(int k=0; k<cofName.length; k++){
            if(cofNameParam.equals("null")){
                cofNameParam = cofName[k] + ", ";
                cofQuanParam = cofQuan[k] + "개, ";
            }else{
                if(cofName[k]!=null){
                    cofNameParam += (cofName[k] + ", ");
                    cofQuanParam += (cofQuan[k] + "개, ");
                }
            }
        }

        locationParam = MainActivity.store;

    }
    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SendDataToWebActivity.this, "please wait", null, true,true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            //String name = (String)params[0];
            //String address = (String)params[1];

            String surl = "http://cafelk.chickenkiller.com/checkList.php";
            String postParameters = "cofName=" + cofNameParam + "&cofQuan=" + cofQuanParam + "&location=" + locationParam + "&userName=" + checkActivity.textByEditText;

            try{
                URL url = new URL(surl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }

                bufferedReader.close();

                return stringBuilder.toString();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
