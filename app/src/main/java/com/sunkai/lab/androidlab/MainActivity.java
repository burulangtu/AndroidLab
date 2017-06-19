package com.sunkai.lab.androidlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.client.android.decode.CaptureActivity;
import com.sunkai.lab.androidlab.diybanner.DIYBannerActivity;
import com.sunkai.lab.androidlab.diyview.DIYViewActivity;
import com.sunkai.lab.androidlab.mixtypeset.MixTextImageActivity;

public class MainActivity extends Activity {

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void diyView(View view) {
        startActivity(new Intent(this, DIYViewActivity.class));
    }

    public void diyBanner(View view) {
        startActivity(new Intent(this, DIYBannerActivity.class));
    }

    public void qrScan(View view) {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void textWithImage(View view) {
        startActivity(new Intent(this, MixTextImageActivity.class));
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
