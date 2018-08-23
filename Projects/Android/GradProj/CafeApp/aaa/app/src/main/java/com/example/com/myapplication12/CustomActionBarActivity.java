package com.example.com.myapplication12;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by com on 2017-04-12.
 */

public class CustomActionBarActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar);

        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_actionbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar)actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);
        parent.setPadding(0, 0, 0, 0);
    }
}
