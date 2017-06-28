package com.sunkai.lab.androidlab.timeline;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.sunkai.lab.androidlab.R;

/**
 * Created by sunkai on 2017/6/28.
 */

public class DotItemDecoration extends RecyclerView.ItemDecoration {
    private int itemInterval = 20;
    Paint mLinePaint;
    private int mLineColor = Color.RED;
    private int mLineWidth = 4;

    private int mDotPaddingTop = 30;
    private int mDotRadius = 5;
    private int mDotColor = Color.WHITE;
    private int mTextColor = Color.WHITE;

    Paint mDotPaint;

    String mEnd = "END";
    Paint mTextPaint;
    Rect mTextRect;
    private int mTextSize = 18;

    private int mDotPaddingText = 10;

    public DotItemDecoration() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);

        mDotPaint = new Paint();
        mDotPaint.setColor(mDotColor);
        mDotPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextRect = new Rect();

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = itemInterval;
        outRect.right = itemInterval;
        outRect.bottom = itemInterval;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 2 * itemInterval;
        } else if (parent.getChildAdapterPosition(view) == 1) {
            outRect.top = 2 * 2 * itemInterval;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams staggerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            int spanIndex = staggerLayoutParams.getSpanIndex();
            if (spanIndex != -1) {
                view.setBackgroundResource(spanIndex == 0 ? R.drawable.pop_left : R.drawable.pop_right);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawLine(c, parent);
        drawDot(c, parent);
    }

    private void drawDot(Canvas c, RecyclerView parent) {
        final int parentWidth = parent.getMeasuredWidth();
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int top = child.getTop() + mDotPaddingTop;
            int bottom;
            int drawableLeft;
            drawableLeft = parentWidth / 2;
            c.drawCircle(drawableLeft, top, mDotRadius, mDotPaint);

            if (i == childCount - 1) {
                View lastChild = parent.getChildAt(i - 1);
                if (lastChild.getBottom() < child.getBottom()) {
                    top = child.getBottom() - mDotPaddingTop;
                    bottom = child.getBottom() + mDotRadius;
                } else {
                    top = lastChild.getBottom() - mDotPaddingTop;
                    bottom = lastChild.getBottom() + mDotRadius;
                }

                c.drawCircle(drawableLeft, top, mDotRadius, mDotPaint);

                mTextPaint.getTextBounds(mEnd, 0, mEnd.length(), mTextRect);
                mTextPaint.setTextSize(mTextSize);
                c.drawText(mEnd, parentWidth / 2 - mTextRect.width() / 2, bottom + mDotPaddingText + mTextSize - mDotPaddingTop, mTextPaint);
            }
        }
    }

    private void drawLine(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int parentWidth = parent.getMeasuredWidth();
        int bottom;

        final int childCount = parent.getChildCount();
        View lastChild = parent.getChildAt(childCount - 1);
        if (childCount > 1) {
            View child = parent.getChildAt(childCount - 2);
            bottom = (child.getBottom() > lastChild.getBottom() ? child.getBottom() : lastChild.getBottom());
        } else {
            bottom = lastChild.getBottom();
        }

        c.drawLine(parentWidth / 2, top, parentWidth / 2, bottom - mDotPaddingTop, mLinePaint);
    }
}
