package com.sunkai.lab.androidlab.patheffect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SumPathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sunkai on 2017/6/22.
 */

public class PathEffectView extends View {
    private Paint mPaint;
    private Path mPath;

    public PathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
    }

    private void initPath() {
        mPath = new Path();
        mPath.moveTo(10, 100);

        mPath.lineTo(100, 200);

        mPath.lineTo(10, 300);

        mPath.lineTo(100, 300);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.DKGRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, 0);
        canvas.drawPath(mPath, mPaint);

        canvas.translate(110, 0);
        mPaint.setPathEffect(new CornerPathEffect(16));
        canvas.drawPath(mPath, mPaint);

        canvas.translate(110, 0);
        mPaint.setPathEffect(new DashPathEffect(new float[]{8, 4, 16, 4}, 6));
        canvas.drawPath(mPath, mPaint);


        canvas.translate(110, 0);
        mPaint.setPathEffect(new ComposePathEffect(
                new CornerPathEffect(24),
                new DashPathEffect(new float[]{8, 4, 16, 4}, 6)
        ));
        canvas.drawPath(mPath, mPaint);

        canvas.translate(110, 0);
        mPaint.setPathEffect(new ComposePathEffect(
                new DashPathEffect(new float[]{8, 4, 16, 4}, 6),
                new CornerPathEffect(24)
        ));
        canvas.drawPath(mPath, mPaint);

        canvas.translate(110, 0);
        mPaint.setPathEffect(new SumPathEffect(
                new DashPathEffect(new float[]{8, 4, 16, 4}, 6),
                new CornerPathEffect(24)
        ));
        canvas.drawPath(mPath, mPaint);


        canvas.translate(-110, 300);
        mPaint.setPathEffect(new DiscretePathEffect(16, 16));
        canvas.drawPath(mPath, mPaint);

        canvas.translate(-110, 0);
        mPaint.setPathEffect(new ComposePathEffect(
                new ComposePathEffect(
                        new DashPathEffect(new float[]{8, 4, 16, 4}, 6),
                        new CornerPathEffect(24)
                ),
                new DiscretePathEffect(16, 16)
        ));
        canvas.drawPath(mPath, mPaint);

        canvas.translate(-110, 0);
        mPaint.setPathEffect(new ComposePathEffect(
                new ComposePathEffect(
                        new CornerPathEffect(24),
                        new DashPathEffect(new float[]{8, 4, 16, 4}, 6)
                ),
                new DiscretePathEffect(16, 16)
        ));
        canvas.drawPath(mPath, mPaint);


    }

}
