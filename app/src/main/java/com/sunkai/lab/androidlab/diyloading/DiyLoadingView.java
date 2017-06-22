package com.sunkai.lab.androidlab.diyloading;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
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
    Path mDragPath;
    private Paint mDragPaint;
    private float mDrag;
    private float mFadeFactor = 10;
    float dragLength;
    private Paint mArrowPaint;

    public DiyLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        path = new Path();
        path.lineTo(500, 0);
        // 这里不能 close  不然 PathDashPathEffect 无法达到动画作用

        mDragPath = makeDragPath(100);
        dragLength = new PathMeasure(mDragPath, false).getLength();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        mPaint.setColor(Color.DKGRAY);

        mDragPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDragPaint.setStyle(Paint.Style.STROKE);
        mDragPaint.setStrokeWidth(10);
        mDragPaint.setColor(Color.DKGRAY);

        mArrowPaint = new Paint(mDragPaint);


        mWaitAnimator = ObjectAnimator.ofFloat(this, "wait", 1.0f, 0.0f).setDuration(4000);
        mWaitAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mWaitAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mWaitAnimator.setInterpolator(new LinearInterpolator());
        mWaitAnimator.start();


        ObjectAnimator.ofFloat(this, "drag", 1.0f, 0.0f).setDuration(4000 / 3).start();
    }

    public void setDrag(float drag) {
        mDrag = drag;

        mDragPaint.setPathEffect(createPathEffect(dragLength, mDrag, 50));
        mArrowPaint.setPathEffect(createArrowPathEffect(dragLength, mDrag, 50));

        int alpha = (int) (Math.min((1.0f - mDrag) * mFadeFactor, 1.0f) * 255.0f);
        mDragPaint.setAlpha(alpha);
        mArrowPaint.setAlpha(alpha);

        invalidate();
    }

    private PathEffect createArrowPathEffect(float pathLength, float phase, float offset) {
        return new PathDashPathEffect(makeArrow(50, 50), pathLength,
                Math.max(phase * pathLength, offset), PathDashPathEffect.Style.ROTATE);
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                Math.max(phase * pathLength, offset));
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

    /**
     * 绘制三角箭头
     *
     * @param length
     * @param height
     * @return
     */
    private static Path makeArrow(float length, float height) {
        Path p = new Path();
        p.moveTo(-2.0f, -height / 2.0f);
        p.lineTo(length, 0.0f);
        p.lineTo(-2.0f, height / 2.0f);
        p.close();
        return p;
    }

    /**
     * 绘制路径
     *
     * @param radius
     * @return
     */
    private static Path makeDragPath(int radius) {
        Path p = new Path();
        RectF oval = new RectF(0.0f, 0.0f, radius * 2.0f, radius * 2.0f);

        float cx = oval.centerX();
        float cy = oval.centerY();
        float rx = oval.width() / 2.0f;
        float ry = oval.height() / 2.0f;

        final float TAN_PI_OVER_8 = 0.414213562f;
        final float ROOT_2_OVER_2 = 0.707106781f;

        float sx = rx * TAN_PI_OVER_8;
        float sy = ry * TAN_PI_OVER_8;
        float mx = rx * ROOT_2_OVER_2;
        float my = ry * ROOT_2_OVER_2;

        float L = oval.left;
        float T = oval.top;
        float R = oval.right;
        float B = oval.bottom;

        p.moveTo(R, cy);
        p.quadTo(R, cy + sy, cx + mx, cy + my);
        p.quadTo(cx + sx, B, cx, B);
        p.quadTo(cx - sx, B, cx - mx, cy + my);
        p.quadTo(L, cy + sy, L, cy);
        p.quadTo(L, cy - sy, cx - mx, cy - my);
        p.quadTo(cx - sx, T, cx, T);
        p.lineTo(cx, T - oval.height() * 1.3f);

        return p;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(100, 100);
        canvas.drawPath(path, mPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(150, 500);
        canvas.drawPath(mDragPath, mDragPaint);
        canvas.drawPath(mDragPath, mArrowPaint);
        canvas.restore();
    }
}
