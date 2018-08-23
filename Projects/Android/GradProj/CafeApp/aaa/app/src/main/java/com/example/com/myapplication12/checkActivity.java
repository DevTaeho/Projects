package com.example.com.myapplication12;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by com on 2017-04-09.
 */

public class checkActivity extends CustomActionBarActivity{

    private NfcAdapter nfcAdapter;

    private static String tagNum = null;

    private PendingIntent pendingIntent;

    private EditText editText;
    static public String textByEditText;

    private int addedTotalPrice;
    /*
    private ArrayList<String> teleData;

    private String[] cofName = new String[10];
    private int[] cofPrice = new int[10];
    private int[] cofQuan = new int[10];
    private int cofNameIndex = 0;
    private int cofPriceIndex = 0;
    private  int cofQuanIndex = 0;
    */
    private FirebaseDatabase database;
    private DatabaseReference nfcRef;

    private String[] getNfcId = new String[30];
    private String[] getNfcInfo = new String[30];
    private int nfcQuan = 0;

    private int checkNfcIndex;
    private boolean findTag = false;

    private ArrayList<String> teleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        editText = (EditText) findViewById(R.id.userName);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        database = FirebaseDatabase.getInstance();
        nfcRef = database.getReference("nfcData");

        nfcRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot tagNum : dataSnapshot.getChildren()){
                    getNfcId[nfcQuan] = String.valueOf(tagNum.getValue());
                    getNfcInfo[nfcQuan] = String.valueOf(tagNum.getKey());
                    nfcQuan++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
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

        for(int i=0; i<cofNameIndex; i++){
            addedTotalPrice += cofPrice[i]*cofQuan[i];
        }
        */

        Intent intent1 = getIntent();
        teleData = intent1.getStringArrayListExtra("teleData");
    }


    protected void onResume() {
        super.onResume();
        //앱이 실행될 때 NFC 어댑터 활성화
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }


    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        Parcelable[] messages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId(); //Byte로 된 태그 ID를 저장

            tagNum = toHexString(tagId);
        }

        for(checkNfcIndex = 0; checkNfcIndex < (nfcQuan+1); checkNfcIndex++){
            if(tagNum.equals(getNfcId[checkNfcIndex]))
                findTag = true;
        }

        if(findTag){
            textByEditText = editText.getText().toString();

            if(textByEditText.equals("이름") || textByEditText.equals("")){
                Toast.makeText(this, "이름을 적어주세요", Toast.LENGTH_LONG).show();
            }else {
                //여기다가 결제 완료를 넘길거임
                //결제 머니를 넘긴 후
                //총 가격, findTag 초기화 (후에 쓰레기 값이 들어갈 것을 대비해서..)
                addedTotalPrice = 0;
                findTag = false;
                //그리고 메인 엑티비티로
                Intent intent2 = new Intent(checkActivity.this, SendDataToWebActivity.class);
                intent2.putExtra("teleData", teleData);
                startActivity(intent2);
            }
        }
        else{
            Toast.makeText(this, "Warning! 가게에서만 사용할 것", Toast.LENGTH_SHORT).show();
            //이건 가게 아닌곳에서 태그질할 때
        }
        findTag = false;
        addedTotalPrice = 0;


    }

    //BYTE 형태의 데이터를 String 형태로 바꿔주는 동작을 수행하는 함수
    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F)
            );
        }
        return sb.toString();
    }

}
