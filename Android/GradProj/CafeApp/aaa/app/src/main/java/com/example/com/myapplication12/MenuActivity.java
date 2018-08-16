package com.example.com.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MenuActivity extends CustomActionBarActivity {

    private int price;
    private String priceString;
    private int totalPrice;
    private int addedTotalPrice;
    private int i = 0;
    private int k = 0;
    private static final int USER_CAN_CHOOSE_QUANTITY = 10;
    private String[] selectedMenu = new String[USER_CAN_CHOOSE_QUANTITY];
    private ArrayList<String> teleData;
    private int selectedMenuIndex = 0;
    private int selectedQuan;
    private String selectedQuanString;

    public static final int MAX_MENU_NUM = 40;

    String[] a = new String[15];
    String[] b = new String[15];
    Bitmap imageicon;

    FirebaseDatabase database;
    DatabaseReference myRef;


    ListView listView;
    ListView listView2;

    ImageView dialogImgView;

    private ArrayList<Listviewitem> test;
    private ArrayList<String> list;
    private ArrayAdapter<String> listviewAdapter;
    private ArrayAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("menu");

        teleData = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.listview);
        listView2 = (ListView) findViewById(R.id.listview2);



        test = new ArrayList<>();

        dataAdapter = new ListviewAdaper(MenuActivity.this, R.layout.activity_item,test);
        list = new ArrayList<String>();
        listviewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(dataAdapter);
        listView2.setAdapter(listviewAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataAdapter.clear();
                for (DataSnapshot menu : dataSnapshot.getChildren()) {
                    String coffeemenu = menu.getKey();
                    String coffeepay = String.valueOf(menu.getValue());

                    a[i] = coffeemenu;
                    b[i] = coffeepay;

                    switch (a[i]) {
                        case "아메리카노":   imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.americano);break;
                        case "에스프레소":   imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.espresso);break;
                        case "카페비엔나":   imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.cafebiena);break;
                        case "카푸치노":    imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.capuchino);break;
                        case "카페라떼":    imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.caferatte);break;
                        case "카페모카":    imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.cafemoca);break;
                        case "샌드위치":    imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.sandwich);break;
                        case "쿠키":  imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.cookie);break;
                        default:    imageicon = BitmapFactory.decodeResource(getResources(), R.drawable.ready);
                    }

                    Listviewitem test1 = new Listviewitem(a[i],b[i],imageicon);
                    dataAdapter.add(test1);
                    i++;
                    imageicon = null;
                }

                dataAdapter.notifyDataSetChanged();
                listView.setSelection(dataAdapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                for(k=0; k<MAX_MENU_NUM; k++){
                if(position == k){
                    selectedMenu[selectedMenuIndex] = a[k];
                    priceString = b[k];
                    price = Integer.parseInt(b[k]);

                    //Dialog에서 보여줄 입력 화면 View 객체 생성
                    LayoutInflater inflater = getLayoutInflater();

                    //레이아웃 리소스 파일로 View 객체 생성
                    final View dialogView= inflater.inflate(R.layout.activity_dialogchart, null);

                    //멤버의 세부 내용 입력 Dialog 생성 및 보이기
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setTitle(listView.getItemAtPosition(position).toString());
                    //builder.setIcon() 타이틀 옆 이미지 추가
                    builder.setView(dialogView); //dialog View 객체 세팅
                    final TextView textView = new TextView(MenuActivity.this);
                    textView.findViewById(R.id.product);
                    textView.setText(listView.getItemAtPosition(position).toString());
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 확인 눌렀을 때 값 넘기기
                            final TextView textView = (TextView)findViewById(R.id.totalPrice);
                            EditText editText = (EditText)dialogView.findViewById(R.id.dialog_edit);
                            if(Pattern.matches("^[0-9]+$", editText.getText().toString()))
                            {
                                selectedMenuIndex++;
                                selectedQuanString = editText.getText().toString();
                                selectedQuan = Integer.parseInt(selectedQuanString);
                                //숫자일 때
                                Loop1 : switch (selectedQuan){
                                    case 1: totalPrice = price * 1;
                                        break;
                                    case 2: totalPrice = price * 2;
                                        break;
                                    case 3: totalPrice = price * 3;
                                        break;
                                    case 4: totalPrice = price * 4;
                                        break;
                                    case 5: totalPrice = price * 5;
                                        break;
                                    case 6: totalPrice = price * 6;
                                        break;
                                    case 7: totalPrice = price * 7;
                                        break;
                                    case 8: totalPrice = price * 8;
                                        break;
                                    case 9: totalPrice = price * 9;
                                        break;
                                    case 10: totalPrice = price * 10;
                                        break;
                                    default:
                                        Toast.makeText(MenuActivity.this, "10개 까지밖에 안됩니다!", Toast.LENGTH_SHORT).show();
                                        break Loop1;
                                }
                                if(totalPrice != 0)
                                    listviewAdapter.add(selectedMenu[selectedMenuIndex-1] +"\t\t"+ Integer.parseInt(editText.getText().toString()) + "개 : " + totalPrice + " 원");

                                teleData.add(selectedMenu[selectedMenuIndex-1]);
                                teleData.add(priceString);
                                teleData.add(selectedQuanString);

                                addedTotalPrice += totalPrice;
                                totalPrice = 0;
                                textView.setText("총\t\t" + addedTotalPrice + "\t원");

                            }else{
                                //숫자가 아닐 때
                                Toast.makeText(MenuActivity.this, "숫자를 입력해주세요!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    //설정한 builder 값으로 dialog 객체 생성
                    AlertDialog dialog = builder.create();
                    //다이얼로그 바깥부분 터치시 꺼지지 않게 설정
                    dialog.setCanceledOnTouchOutside(false);
                    //다이얼로그 보여주기
                    dialog.show();
                }

            }}
        });
    }

    public void onClick1(View v){
        if(addedTotalPrice == 0){
            Toast.makeText(this, "주문하실 물품을 선택해 주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent1 = new Intent(MenuActivity.this, checkActivity.class);
            intent1.putExtra("teleData", teleData);
            startActivity(intent1);
        }
    }
}
