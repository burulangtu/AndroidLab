package com.sunkai.lab.androidlab.fish;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.sunkai.lab.androidlab.R;

public class FishActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_fish);
    }
}
