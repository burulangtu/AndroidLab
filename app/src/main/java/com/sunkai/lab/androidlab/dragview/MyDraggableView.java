package com.sunkai.lab.androidlab.dragview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by sunkai on 2018/1/10.
 */

public class MyDraggableView extends RelativeLayout {
    private static final String TAG = MyDraggableView.class.getSimpleName();
    private ViewDragHelper viewDragHelper;

    public MyDraggableView(Context context) {
        super(context);
        initView();
    }

    public MyDraggableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyDraggableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyDraggableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new DraggableViewCallback(this));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        //TODO

    }

    public void closeToBottom(View view ) {
        Log.e(TAG, "closeToBottom" + ",getHeight:" + getHeight());
        if (viewDragHelper.smoothSlideViewTo(view, 0, getHeight() - 500)) {
            ViewCompat.postInvalidateOnAnimation(this);
            notifyClosedToBottomListener();
        }

    }

    private void notifyClosedToBottomListener() {
        Log.e(TAG, "notifyClosedToBottomListener");
    }

    public void onReset() {
        Log.e(TAG, "onReset");
        viewDragHelper.settleCapturedViewAt(0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
