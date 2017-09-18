package com.sunkai.lab.androidlab.dragball;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * 仿QQ消息红点拖拽效果view
 * <p>
 * copy from http://www.jianshu.com/p/ed2721286778
 */

public class DragBallView extends View {

    // 绘制图形画笔
    private Paint dragBallPaint;

    // 图形颜色
    private int circleColor = Color.RED;

    // 贝塞尔曲线 path
    private Path path;

    // 起点位置
    private int startX;
    private int startY;

    // 小球初始大小
    private float ballRadius;
    //两圆相离最大距离
    private float maxDistance;

    //是否可拖拽
    private boolean mIsCanDrag = false;
    //是否超过最大距离
    private boolean isOutOfRang = false;
    //最终圆是否消失
    private boolean disappear = false;


    //贝塞尔曲线需要的点
    private PointF pointA;
    private PointF pointB;
    private PointF pointC;
    private PointF pointD;
    //控制点坐标
    private PointF pointO;

    //起始位置点
    private PointF pointStart;
    //拖拽位置点
    private PointF pointEnd;

    //根据滑动位置动态改变圆的半径
    private float currentRadiusStart;
    private float currentRadiusEnd;


    public DragBallView(Context context) {
        this(context, null);
    }

    public DragBallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initPoint();

    }

    /**
     * 初始化所有点
     */
    private void initPoint() {
        pointStart = new PointF(startX, startY);
        pointEnd = new PointF(startX, startY);
        pointA = new PointF();
        pointB = new PointF();
        pointC = new PointF();
        pointD = new PointF();
        pointO = new PointF();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        dragBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dragBallPaint.setColor(circleColor);
        dragBallPaint.setAntiAlias(true);
        dragBallPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = w / 2;
        startY = h / 2;
        maxDistance = dp2px(150);
        ballRadius = dp2px(25);
        currentRadiusEnd = currentRadiusStart = ballRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pointStart.set(startX, startY);
        if (isOutOfRang) {
            if (!disappear) {
                drawBall(canvas, pointEnd, currentRadiusEnd);
            }
        } else {
            drawBall(canvas, pointStart, currentRadiusStart);
            if (mIsCanDrag) {
                drawBall(canvas, pointEnd, currentRadiusEnd);
                drawBezier(canvas);
            }

        }
    }


    /**
     * 画起始小球
     *
     * @param canvas 画布
     * @param pointF 点坐标
     * @param radius 半径
     */
    private void drawBall(Canvas canvas, PointF pointF, float radius) {
        canvas.drawCircle(pointF.x, pointF.y, radius, dragBallPaint);
    }

    /**
     * 画贝塞尔曲线
     *
     * @param canvas 画布
     */
    private void drawBezier(Canvas canvas) {
        path.reset();
        path.moveTo(pointA.x, pointA.y);
        path.quadTo(pointO.x, pointO.y, pointB.x, pointB.y);
        path.lineTo(pointC.x, pointC.y);
        path.quadTo(pointO.x, pointO.y, pointD.x, pointD.y);
        path.lineTo(pointA.x, pointA.y);
        path.close();

        canvas.drawPath(path, dragBallPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float currentX;
        float currentY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                setIsCanDrag(event);
                break;
            case MotionEvent.ACTION_MOVE:

                if (mIsCanDrag) {

                    currentX = event.getX();
                    currentY = event.getY();
                    //设置拖拽圆的坐标
                    pointEnd.set(currentX, currentY);

                    if (!isOutOfRang) {
                        setCurrentRadius();
                        setABCDOPoint();
                    }

                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mIsCanDrag) {
                    if (isOutOfRang) {
                        //消失动画
                        disappear = true;
                        invalidate();
                    } else {
                        disappear = false;
                        //回弹动画
                        final float a = (pointEnd.y - pointStart.y) / (pointEnd.x - pointStart.x);
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(pointEnd.x, pointStart.x);
                        valueAnimator.setDuration(500);
                        valueAnimator.setInterpolator(new BounceInterpolator());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float x = (float) animation.getAnimatedValue();

                                float y = pointStart.y + a * (x - pointStart.x);

                                pointEnd.set(x, y);
                                setCurrentRadius();

                                setABCDOPoint();

                                invalidate();

                            }
                        });
                        valueAnimator.start();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设置当前计算的到的半径
     */
    private void setCurrentRadius() {
        //两个圆心之间的距离
        float distance = (float) Math.sqrt(Math.pow(pointStart.x - pointEnd.x, 2) + Math.pow(pointStart.y - pointEnd.y, 2));

        //拖拽距离在设置的最大值范围内才绘制贝塞尔图形
        if (distance <= maxDistance) {
            //比例系数  控制两圆半径缩放
            float percent = distance / maxDistance;

            //之所以*0.6和0.2只为了放置拖拽过程圆变化的过大和过小这个系数是多次尝试的出的
            //你也可以适当调整系数达到自己想要的效果
            currentRadiusStart = (1 - percent * 0.6f) * ballRadius;
            currentRadiusEnd = (1 + percent * 0.2f) * ballRadius;

            isOutOfRang = false;
        } else {
            isOutOfRang = true;
            currentRadiusStart = ballRadius;
            currentRadiusEnd = ballRadius;
        }
    }

    /**
     * 判断是否可以拖拽
     *
     * @param event event
     */
    private void setIsCanDrag(MotionEvent event) {
        Rect rect = new Rect();
        rect.left = (int) (startX - ballRadius);
        rect.top = (int) (startY - ballRadius);
        rect.right = (int) (startX + ballRadius);
        rect.bottom = (int) (startY + ballRadius);

        //触摸点是否在圆的坐标域内
        mIsCanDrag = rect.contains((int) event.getX(), (int) event.getY());
    }

    /**
     * 设置贝塞尔曲线的相关点坐标  计算方式参照结算图即可看明白
     * （ps为了画个清楚这个图花了不少功夫哦）
     */
    private void setABCDOPoint() {
        //控制点坐标
        pointO.set((pointStart.x + pointEnd.x) / 2.0f, (pointStart.y + pointEnd.y) / 2.0f);

        float x = pointEnd.x - pointStart.x;
        float y = pointEnd.y - pointStart.y;

        //斜率 tanA=rate
        double rate;
        rate = x / y;
        //角度  根据反正切函数算角度
        float angle = (float) Math.atan(rate);

        pointA.x = (float) (pointStart.x + Math.cos(angle) * currentRadiusStart);
        pointA.y = (float) (pointStart.y - Math.sin(angle) * currentRadiusStart);

        pointB.x = (float) (pointEnd.x + Math.cos(angle) * currentRadiusEnd);
        pointB.y = (float) (pointEnd.y - Math.sin(angle) * currentRadiusEnd);

        pointC.x = (float) (pointEnd.x - Math.cos(angle) * currentRadiusEnd);
        pointC.y = (float) (pointEnd.y + Math.sin(angle) * currentRadiusEnd);

        pointD.x = (float) (pointStart.x - Math.cos(angle) * currentRadiusStart);
        pointD.y = (float) (pointStart.y + Math.sin(angle) * currentRadiusStart);
    }


    public void reset() {
        mIsCanDrag = false;
        isOutOfRang = false;
        disappear = false;
        pointStart.set(startX, startY);
        pointEnd.set(startX, startY);

        setABCDOPoint();
        invalidate();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
