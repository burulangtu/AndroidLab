package com.sunkai.lab.androidlab.diyloading;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by sunkai on 2017/6/20.
 */

public class DiyLoadingView extends View {
    private Paint mPaint;
    Path path;
    private float mWait;
    private ObjectAnimator mWaitAnimator;

    public DiyLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        path = new Path();
        path.lineTo(500, 0);
        // 这里不能 close  不然 PathDashPathEffect 无法达到动画作用
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        mPaint.setColor(Color.DKGRAY);

        mWaitAnimator = ObjectAnimator.ofFloat(this, "wait", 1.0f, 0.0f).setDuration(4000);
        mWaitAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mWaitAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mWaitAnimator.setInterpolator(new LinearInterpolator());
        mWaitAnimator.start();
    }


    public void setWait(float wait) {
        mWait = wait;
        mPaint.setPathEffect(createConcaveArrowPathEffect(500, mWait, 32.0f));

        invalidate();
    }

    private PathEffect createConcaveArrowPathEffect(float pathLength, float phase, float offset) {
        return new PathDashPathEffect(makeConcaveArrow(50, 50), 50 * 1.2f, Math.max(phase * pathLength, offset), PathDashPathEffect.Style.ROTATE);
    }

    /**
     * 绘制剪头图形
     *
     * @param length
     * @param height
     * @return
     */
    private static Path makeConcaveArrow(float length, float height) {
        Path p = new Path();
        p.moveTo(-2.0f, -height / 2.0f);
        p.lineTo(length - height / 4.0f, -height / 2.0f);
        p.lineTo(length, 0.0f);
        p.lineTo(length - height / 4.0f, height / 2.0f);
        p.lineTo(-2.0f, height / 2.0f);
        p.lineTo(-2.0f + height / 4.0f, 0.0f);
        p.close();
        return p;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(100, 100);
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }
}
