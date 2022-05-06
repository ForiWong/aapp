package com.wlp.myapplication.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wlp.myapplication.R;

public class LifeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);

        // 创建定位服务
        new MyLocation(getLifecycle());
    }
}