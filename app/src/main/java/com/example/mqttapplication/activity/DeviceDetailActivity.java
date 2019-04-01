package com.example.mqttapplication.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mqttapplication.R;

public class DeviceDetailActivity extends AppCompatActivity {
    private Toolbar devicetoolbar;
    private String title;
    private int background;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        background = intent.getIntExtra("BkgToolbar", 0);

        devicetoolbar = findViewById(R.id.device_detail_toolbar);
        setSupportActionBar(devicetoolbar);
        getSupportActionBar().setTitle(title);
        devicetoolbar.setSubtitle("Connect to " + "mainserver.com");
        //Call function to set background for Toolbar
        setBackgroundToolbar(background);

    }

    private void setBackgroundToolbar(int colorNum){
        switch(background){
            case 1:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_1);
                break;
            case 2:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_2);
                break;
            case 3:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_3);
                break;
            case 4:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_4);
                break;
            case 5:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_5);
                break;
            case 6:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_6);
                break;
            case 7:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_7);
                break;
            case 8:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_8);
                break;
            default:
                devicetoolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_8);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.device_item_menu){
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
