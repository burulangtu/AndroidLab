/**
 * Copyright 2013 Romain Guy
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunkai.lab.androidlab.diyscroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class StateView extends ImageView {
    private final Object mSvgLock = new Object();
    private float mOffsetY;

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        synchronized (mSvgLock) {
            setTranslationY(getPaddingTop() + mOffsetY);
        }
    }

    public void reveal(View scroller, int parentBottom) {
        float previousOffset = mOffsetY;
        mOffsetY = Math.min(0, scroller.getHeight() - (parentBottom - scroller.getScrollY()));
        if (previousOffset != mOffsetY) invalidate();
    }
}
