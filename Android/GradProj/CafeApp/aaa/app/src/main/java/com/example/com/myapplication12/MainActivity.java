package com.example.com.myapplication12;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends CustomActionBarActivity{
    public int shit = 0;
    private long BackPressed;
    private WifiConfiguration wifiConfiguration;

    private FirebaseDatabase database;
    private DatabaseReference wifiRef;

    private int cafeNumIndex = 0;
    private int correctIndex;
    private int findIndex;
    private String[] cafeWifiName = new String[50];
    private String[] cafeWifiPass = new String[50];

    static public String store="지점선택";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button3).setOnClickListener(mClickListener);

        //Making the object for wifi configuration
        wifiConfiguration = new WifiConfiguration();

        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Making the reference for using Firebase database.
        database = FirebaseDatabase.getInstance();
        wifiRef = database.getReference("storeWifi");

        wifiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot wifiName : dataSnapshot.getChildren()){
                    cafeWifiName[cafeNumIndex] = String.valueOf(wifiName.getKey());
                    cafeWifiPass[cafeNumIndex] = String.valueOf(wifiName.getValue());
                    cafeNumIndex++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    //This is an Event when click is executed.
    //If the button called 'wifi connect' in UI is clicked, your device connect to the store's wifi.
    //The wifi data will be taken from firebase database called 'wifiName' by app.(This is not implemented yet in this version.)
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            for(findIndex = 0; findIndex < (cafeNumIndex+1); findIndex++){
                if(cafeWifiName[findIndex].equals(store)) {
                    correctIndex = findIndex;
                    break;
                }
            }
            String ssid = cafeWifiName[correctIndex];
            String pw = cafeWifiPass[correctIndex];

            if(cafeWifiName[correctIndex].equals("지점선택")){}
                else{
                    Toast.makeText(MainActivity.this,cafeWifiName[correctIndex] + "와이파이에 접속중",Toast.LENGTH_SHORT).show();
                }

            wifiConfiguration.SSID = "\"".concat(ssid).concat("\"");
            wifiConfiguration.status = WifiConfiguration.Status.DISABLED;
            wifiConfiguration.priority = 40;

            wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfiguration.preSharedKey = "\"".concat(pw).concat("\"");

            WifiManager wfMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (!wfMgr.isWifiEnabled()) {
                if (wfMgr.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
                    wfMgr.setWifiEnabled(true);
                }
            }
            int networkId = wfMgr.addNetwork(wifiConfiguration);
            if (networkId != -1) {

            }
            wfMgr.removeNetwork(networkId);
            wfMgr.addNetwork(wifiConfiguration);
        }
    };



    public void onClick(View v){
        if(!store.equals("지점선택")) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "먼저 지점을 선택해 주세요!!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - BackPressed < 1500) {
            ActivityCompat.finishAffinity(this);
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
        BackPressed = System.currentTimeMillis();
    }
}
