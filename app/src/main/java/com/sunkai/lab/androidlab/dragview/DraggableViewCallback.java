package com.sunkai.lab.androidlab.dragview;

import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.View;

/**
 * Created by sunkai on 2018/1/10.
 */

class DraggableViewCallback extends ViewDragHelper.Callback {

    private static final String TAG = DraggableViewCallback.class.getSimpleName();
    private static float Y_MIN_VELOCITY = 300;
    private MyDraggableView myDraggableView;

    public DraggableViewCallback(MyDraggableView myDraggableView) {
        this.myDraggableView = myDraggableView;
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return true;
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        return Math.max(top, 0);
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        myDraggableView.onViewPositionChanged(changedView, left, top, dx, dy);
    }

    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
        return 0;
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
        super.onViewReleased(releasedChild, xvel, yvel);
        Log.e(TAG, "onViewReleased");

        int top = releasedChild.getTop();
        int left = releasedChild.getLeft();
        Log.e(TAG, "onViewReleased,===> top:" + top + ",left:" + left);
        if (Math.abs(left) < Math.abs((top))) {
            triggerOnReleaseActionsWhileVerticalDrag(top, releasedChild);
        }
    }

    private void triggerOnReleaseActionsWhileVerticalDrag(int yVel, View view) {
        if (yVel > 0 && yVel >= Y_MIN_VELOCITY) {
            myDraggableView.closeToBottom(view);
            Log.e(TAG, "ReleaseVerticalDrag" + ", closeToBottom");
        } else {
            myDraggableView.onReset();
            Log.d(TAG, "ReleaseVerticalDrag" + ", onReset");
        }
    }
}
