package com.sunkai.lab.androidlab.qrscan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.sunkai.lab.androidlab.R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 定制化显示扫描界面
 */
public class QrActivity extends FragmentActivity {

    private CaptureFragment captureFragment;
    private static final int REQUEST_CODE_CAMERA = 2;
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        requestPermission(this);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.qr_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            QrActivity.this.setResult(RESULT_OK, resultIntent);
            QrActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            QrActivity.this.setResult(RESULT_OK, resultIntent);
            QrActivity.this.finish();
        }
    };

    void requestPermission(Context context) {
        if (targetVersion(context) >= 23 && Build.VERSION.SDK_INT >= 23) {
            verifyLocationPermission(context);
        }
    }

    /**
     * 判断定位权限并且申请定位权限
     *
     * @param context
     */
    public static void verifyLocationPermission(Context context) {
        int permission = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_CAMERA,
                    REQUEST_CODE_CAMERA);
        }
    }

    public static int targetVersion(Context context) {
        int targetSdkVersion = context.getApplicationContext().getApplicationInfo().targetSdkVersion;
        return targetSdkVersion;
    }
}
