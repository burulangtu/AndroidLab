package com.sunkai.lab.androidlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.zxing.client.android.decode.CaptureActivity;
import com.sunkai.lab.androidlab.diybanner.DIYBannerActivity;
import com.sunkai.lab.androidlab.diyloading.DiyLoadingActivity;
import com.sunkai.lab.androidlab.diyscroller.DIYSliderActivity;
import com.sunkai.lab.androidlab.diyview.DIYViewActivity;
import com.sunkai.lab.androidlab.fish.FishActivity;
import com.sunkai.lab.androidlab.miclock.MiClockActivity;
import com.sunkai.lab.androidlab.mixtypeset.MixTextImageActivity;
import com.sunkai.lab.androidlab.patheffect.PathEffectActivity;
import com.sunkai.lab.androidlab.dragball.DragBallActivity;
import com.sunkai.lab.androidlab.sensor.SensorActivity;
import com.sunkai.lab.androidlab.timeline.TimeLineActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    RecyclerView labList;

    LabListAdapter labListAdapter;


    List<String> labItemTitles = new ArrayList<>();

    {
        labItemTitles.add("自定义View，水位动画");
        labItemTitles.add("自定义View, loading动画");
        labItemTitles.add("自定义View, 轮播banner");
        labItemTitles.add("二维码扫描");
        labItemTitles.add("图文混排");
        labItemTitles.add("PathEffect demo");
        labItemTitles.add("自定义滑动组件");
        labItemTitles.add("recycleView时间轴组件");
        labItemTitles.add("仿小米时钟");
        labItemTitles.add("加速传感器");
        labItemTitles.add("自定义View,小金鱼");
        labItemTitles.add("自定义View,未读消息拖拽粘性效果");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        labList = (RecyclerView) findViewById(R.id.lab_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        labList.setLayoutManager(linearLayoutManager);
        labList.setHasFixedSize(true);
        labListAdapter = new LabListAdapter(labItemTitles);
        labList.setAdapter(labListAdapter);
        labListAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick((Integer) view.getTag());
            }
        });
    }

    private void handleClick(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, DIYViewActivity.class);
                break;
            case 1:
                intent = new Intent(this, DiyLoadingActivity.class);
                break;
            case 2:
                intent = new Intent(this, DIYBannerActivity.class);
                break;
            case 3:
                intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                return;
            case 4:
                intent = new Intent(this, MixTextImageActivity.class);
                break;
            case 5:
                intent = new Intent(this, PathEffectActivity.class);
                break;
            case 6:
                intent = new Intent(this, DIYSliderActivity.class);
                break;
            case 7:
                intent = new Intent(this, TimeLineActivity.class);
                break;
            case 8:
                intent = new Intent(this, MiClockActivity.class);
                break;
            case 9:
                intent = new Intent(this, SensorActivity.class);
                break;
            case 10:
                intent = new Intent(this, FishActivity.class);
            case 11:
                intent = new Intent(this, DragBallActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                Log.e("SK", data.getStringExtra("result"));
            }
        }
    }


}
