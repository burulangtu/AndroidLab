package com.sunkai.lab.androidlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunkai.lab.androidlab.diyview.DIYViewActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void diyView(View view) {
        startActivity(new Intent(this, DIYViewActivity.class));
    }
}
