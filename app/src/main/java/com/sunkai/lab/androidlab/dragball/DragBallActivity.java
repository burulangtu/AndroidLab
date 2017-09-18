package com.sunkai.lab.androidlab.dragball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sunkai.lab.androidlab.R;

public class DragBallActivity extends Activity {

    DragBallView dragBallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ball);
        dragBallView = (DragBallView) findViewById(R.id.dragBallView);
    }

    public void reset(View view) {
        dragBallView.reset();
    }
}
