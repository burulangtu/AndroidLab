package com.sunkai.lab.androidlab.diyview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by sunkai on 2017/4/14.
 * <p>
 * 核心方法：
 * 使用『正弦曲线公式』绘制波浪，利用属性动画实现波浪动态效果。
 * <p>
 * https://zh.wikipedia.org/zh-cn/%E6%AD%A3%E5%BC%A6%E6%9B%B2%E7%BA%BF
 * <p>
 * y=Asin(ωx+φ)+h
 * <p>
 * A 表示浪花波幅
 * ω 表示角频率
 * h 表示 y 轴偏移量
 */

public class WaveView extends View {
    /**
     * y轴偏移量，表示水位位置
     */
    int waterLevel;

    private float waveShiftRatio = 0.0f;
    /**
     * 控制水位高度
     */
    private float waterLevelRatio = 0f;
    /**
     * 波浪半径
     */
    private float waveRadius;
    /**
     * 负责变换的矩阵
     */
    private Matrix shaderMatrix;

    /**
     * 绘制波浪的画笔
     */
    private Paint wavePaint;

    AnimatorSet mAnimatorSet;


    public WaveView(Context context) {
        super(context);
        initData();
    }

    private void initData() {
        shaderMatrix = new Matrix();
        wavePaint = new Paint();
        initAnimation();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        shaderMatrix.postTranslate(100, 10);
        shaderMatrix.setScale(1, (float) 0.25, 0, waterLevel);
        shaderMatrix.postTranslate(waveShiftRatio * getWidth(), (0.5F - waterLevelRatio) * getHeight());
        wavePaint.getShader().setLocalMatrix(shaderMatrix);

        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, waveRadius, wavePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        waveRadius = getWidth() / 2f;
        updateWaveShader();
    }

    /**
     * 绘制波浪的 shader
     * <p>
     * y=Asin(ωx+φ)+h
     * <p>
     * A 表示浪花波幅
     * ω 表示角频率
     * h 表示 y 轴偏移量
     */
    private void updateWaveShader() {
        int width = (int) waveRadius;
        int height = (int) waveRadius;
        if (width > 0 && height > 0) {
            /**
             * 角频率 ，越大表示周期越短。
             * ω = 2π/T
             */
            double angularFrequency = Math.PI / width;

            /**
             * 波幅，越大表示 y 轴的波动越大
             */
            float amplitude = height / 2F;


            waterLevel = getHeight() / 2;
            float defaultWaveLength = width;

            Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            wavePaint.reset();
            wavePaint.setStrokeWidth(2);
            wavePaint.setAntiAlias(true);

            final int endX = getMeasuredWidth() + 1;
            final int endY = getMeasuredHeight() + 1;
            float[] waveY = new float[endX];
            wavePaint.setColor(adjustAlpha(0xff00ddff, 0.3f));
            for (int beginX = 0; beginX < endX; beginX++) {
                float beginY = (float) (waterLevel + amplitude * Math.sin(beginX * angularFrequency));
                canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
                waveY[beginX] = beginY;
            }

            wavePaint.setColor(0xff00ddff);
            final int wave2Shift = (int) (defaultWaveLength / 4);
            for (int beginX = 0; beginX < endX; beginX++) {
                canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
            }
//            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, 50, wavePaint);
            BitmapShader waveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            wavePaint.setShader(waveShader);
        }

    }

    public float getWaveShiftRatio() {
        return waveShiftRatio;
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        if (this.waveShiftRatio != waveShiftRatio) {
            this.waveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return waterLevelRatio;
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (this.waterLevelRatio != waterLevelRatio) {
            this.waterLevelRatio = waterLevelRatio;
        }

    }

    private void initAnimation() {
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(waveShiftAnim);

        Log.e("SK", "play----");
    }

    public void startAnimation() {
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", waterLevelRatio, 0.5F);
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        waterLevelAnim.start();
        if (!mAnimatorSet.isStarted()) {
            mAnimatorSet.start();
        }
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }


    public void riseWater() {
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", waterLevelRatio, 1F);
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        animatorSetProgress.start();
    }

    public void reset() {
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", waterLevelRatio, 0F);
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        animatorSetProgress.start();
    }
}
