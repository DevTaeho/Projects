package com.example.dhfls.testmikepenzandviewpager.iconactivity.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dhfls.testmikepenzandviewpager.DatePickerFragment;
import com.example.dhfls.testmikepenzandviewpager.NumberPickerDialog;
import com.example.dhfls.testmikepenzandviewpager.R;
import com.example.dhfls.testmikepenzandviewpager.loginandsession.SessionControl;

import java.util.ArrayList;

public class AttractionGroup extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    String imageUrl;
    private ImageView attractionImageVIew;
    Toolbar toolbar;
    TextView toolbarTitleTextView;
    Intent getToolbarTitleIntent;
    String toolbarTitle;

    ImageView searchDateImageView;
    ImageView searchDueDateImageView;
    ImageView durationImageView;
    TextView durationTextView;
    TextView groupHostTextView;
    Button inviteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_group);

        // Link those views.
        toolbar = (Toolbar)findViewById(R.id.toolbarGroup);
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title_groupActivity);

        searchDateImageView = (ImageView) findViewById(R.id.ic_date);
        searchDueDateImageView = (ImageView) findViewById(R.id.ic_alertdate);
        durationImageView = (ImageView) findViewById(R.id.ic_duration);
        durationTextView = (TextView) findViewById(R.id.group_travel_duration);
        inviteButton = (Button) findViewById(R.id.button_invitetogroup);
        groupHostTextView = (TextView)findViewById(R.id.group_host_name);


        // Toolbar setting.
        getToolbarTitleIntent = getIntent();
        toolbarTitle = getToolbarTitleIntent.getExtras().getString("toolbarTitle");
        toolbarTitleTextView.setText(toolbarTitle);

        // Image which comes from previous activity is on setting.
        imageUrl = (String) getIntent().getStringExtra("imageUrl");
        attractionImageVIew = (ImageView) findViewById(R.id.attractionImage_group);
        Glide
                .with(this)
                .load(imageUrl)
                .into(attractionImageVIew);

        groupHostTextView.setText(SessionControl.getUserName(this));

        // Click Event Gathering.
        searchDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker("date");
            }
        });

        durationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumberPicker();
            }
        });

        searchDueDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker("duedate");
            }
        });




    } // End onCreate

    public void showDatePicker(String tag){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        durationTextView.setText((numberPicker.getValue()-1)+"박 " + numberPicker.getValue()+"일");
    }

    public void showNumberPicker(){
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(),"time picker");
    }
}
