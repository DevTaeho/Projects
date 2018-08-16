package com.example.dhfls.testmikepenzandviewpager.loginandsession;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dhfls.testmikepenzandviewpager.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText createNAME, createID, createPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        createNAME = (EditText)findViewById(R.id.create_name);
        createID = (EditText)findViewById(R.id.create_email);
        createPW = (EditText)findViewById(R.id.create_password);

    }

    public void insert(View view){

        String Name = createNAME.getText().toString().trim();
        String Id = createID.getText().toString().trim();
        String Pw = createPW.getText().toString().trim();

        String NamePattern = "^[a-zA-Z가-힣]*$";

        if(!Patterns.EMAIL_ADDRESS.matcher(Id).matches()&&!Name.matches(NamePattern)){
            Toast.makeText(getApplicationContext(),"Invalid Name & email address",Toast.LENGTH_SHORT).show();
        }else if(!Name.matches(NamePattern)){
            Toast.makeText(getApplicationContext(),"Invalid Name",Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(Id).matches()){
            Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
        }else {
            insertToDB(Name, Id, Pw);
        }
    }

    private void insertToDB(String Name, String Id, String Pw){
        class InsertData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(SignupActivity.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("success")){
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try{
                    String Name = (String) params[0];
                    String Id = (String) params[1];
                    String Pw = (String) params[2];

                    String link = "http://192.168.0.3/post.php";
                    String data = URLEncoder.encode("Name","UTF-8") + "=" + URLEncoder.encode(Name,"UTF-8");
                    data += "&" + URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }catch (Exception e){
                    return new String ("Exception : " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Name, Id, Pw);
    }
}
