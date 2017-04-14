package com.sunkai.lab.androidlab.diyview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sunkai.lab.androidlab.R;

public class DIYViewActivity extends Activity {

    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diyview);
        waveView = (WaveView) findViewById(R.id.wave);
    }

    public void startAnim(View view) {
        waveView.startAnimation();
    }

    public void riseWater(View view){
        waveView.riseWater();
    }

    public void fallWater(View view){
        waveView.reset();
    }
}

