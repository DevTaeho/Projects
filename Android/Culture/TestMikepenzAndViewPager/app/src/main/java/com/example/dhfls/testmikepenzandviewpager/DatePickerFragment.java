package com.example.dhfls.testmikepenzandviewpager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    TextView dateTextView;
    TextView dueDateTextView;

    String tag;
    static final String DATE_TAG = "date";
    static final String DUE_DATE_TAG = "duedate";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        tag = getTag();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int i, int i1, int i2) {
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();

            dateTextView = (TextView) getActivity().findViewById(R.id.group_travel_date);
            dueDateTextView = (TextView) getActivity().findViewById(R.id.group_travel_dateofend);

            int nowYear = today.year;
            int nowMonth = today.month + 1;
            int nowDay = today.monthDay;


            if(tag.equals(DATE_TAG)){
                if(nowYear > view.getYear()){
                    dateTextView.setText("오늘 및 오늘 이후로만 가능");
                }else{
                    if(nowMonth > (view.getMonth()+1)){
                        dateTextView.setText("오늘 및 오늘 이후로만 가능");
                    }else{
                        if(nowMonth == view.getMonth()+1){
                            if(nowDay > view.getDayOfMonth()){
                                Log.d("nowDay", nowDay+"");
                                dateTextView.setText("오늘 및 오늘 이후로만 가능");
                            }else{
                                dateTextView.setText(view.getYear() + " 년 " + (view.getMonth()+1) + " 월 " + view.getDayOfMonth() + " 일");
                            }
                        }else {
                            dateTextView.setText(view.getYear() + " 년 " + (view.getMonth()+1) + " 월 " + view.getDayOfMonth() + " 일");
                        }
                    }
                }
            }

            if(tag.equals(DUE_DATE_TAG)){
                if(dateTextView.getText().toString().matches("") || dateTextView.getText().toString().matches("오늘 및 오늘 이후로만 가능")){
                    dueDateTextView.setText("날짜를 먼저 선택하세요");
                }else{
                    if(nowYear > view.getYear()){
                        dueDateTextView.setText("오늘 및 오늘 이후로만 가능");
                    }else{
                        if(nowMonth > (view.getMonth()+1)){
                            dueDateTextView.setText("오늘 및 오늘 이후로만 가능");
                        }else{
                            if(nowMonth == view.getMonth()+1){
                                if(nowDay > view.getDayOfMonth()){
                                    dueDateTextView.setText("오늘 및 오늘 이후로만 가능");
                                }else{
                                    String date = dateTextView.getText().toString();
                                    String startYear = date.substring(0,date.indexOf("년")-1);
                                    String startMonth = date.substring(date.indexOf("년")+2);
                                    startMonth = startMonth.substring(0, startMonth.indexOf("월")-1);
                                    String startDay = date.substring(date.indexOf("월")+2);
                                    startDay = startDay.substring(0, startDay.indexOf("일")-1);

                                    int year, month, day;
                                    year = Integer.parseInt(startYear);
                                    month = Integer.parseInt(startMonth);
                                    day = Integer.parseInt(startDay);

                                    Log.d("startYear", year+""+month+""+day);

                                    int dueDay = view.getDayOfMonth();

                                    if(dueDay > day){
                                        dueDateTextView.setText("여행날짜보다 이전으로 하세요");
                                    }else{
                                        dueDateTextView.setText(view.getYear() + " 년 " + (view.getMonth()+1) + " 월 " + view.getDayOfMonth() + " 일");
                                    }
                                }
                            }else {
                                String date = dateTextView.getText().toString();
                                String startYear = date.substring(0,date.indexOf("년")-1);
                                String startMonth = date.substring(date.indexOf("년")+2);
                                startMonth = startMonth.substring(0, startMonth.indexOf("월")-1);
                                String startDay = date.substring(date.indexOf("월")+2);
                                startDay = startDay.substring(0, startDay.indexOf("일")-1);

                                int year, month, day;
                                year = Integer.parseInt(startYear);
                                month = Integer.parseInt(startMonth);
                                day = Integer.parseInt(startDay);

                                int dueYear = view.getYear();
                                int dueMonth = view.getMonth() + 1;
                                int dueDay = view.getDayOfMonth();

                                Log.d("startYear", year+""+month+""+day);

                                if(dueYear > year){
                                    dueDateTextView.setText("여행날짜보다 이전으로 하세요");
                                }else{
                                    if(dueMonth > month){
                                        dueDateTextView.setText("여행날짜보다 이전으로 하세요");
                                    }else{
                                        if(dueDay > day){
                                            dueDateTextView.setText("여행날짜보다 이전으로 하세요");
                                        }else{
                                            dueDateTextView.setText(view.getYear() + " 년 " + (view.getMonth()+1) + " 월 " + view.getDayOfMonth() + " 일");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };
}
