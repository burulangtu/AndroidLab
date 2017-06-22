package com.sunkai.lab.androidlab.diyscroller;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.sunkai.lab.androidlab.R;

import java.util.ArrayList;
import java.util.List;

public class DIYSliderActivity extends Activity {

    private static class State {
        int background;
        int photos[];
        final List<Bitmap> bitmaps = new ArrayList<Bitmap>();

        State(int background, int[] photos) {
            this.background = background;
            this.photos = photos;
        }
    }

    private final State[] mStates = {
            new State(R.color.az, new int[]{
                    R.mipmap.photo_01_antelope,
                    R.mipmap.photo_09_horseshoe,
                    R.mipmap.photo_10_sky
            }),
            new State(R.color.ut, new int[]{
                    R.mipmap.photo_08_arches,
                    R.mipmap.photo_03_bryce,
                    R.mipmap.photo_04_powell,
            }),
            new State(R.color.ca, new int[]{
                    R.mipmap.photo_07_san_francisco,
                    R.mipmap.photo_02_tahoe,
                    R.mipmap.photo_05_sierra,
                    R.mipmap.photo_06_rockaway
            }),
    };

    private View mIntroView;
    private final Rect mTempRect = new Rect();
    private Drawable mWindowBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_slider);
        mIntroView = findViewById(R.id.intro);
        loadPhotos();


        ((TrackingScrollView) findViewById(R.id.scroller)).setOnScrollChangedListener(
                new TrackingScrollView.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged(TrackingScrollView source, int l, int t, int ol, int ot) {
                        handleScroll(source, t);
                    }
                });
    }

    private void handleScroll(ViewGroup source, int top) {
        final float actionBarHeight = getActionBar().getHeight();
        final float firstItemHeight = findViewById(R.id.scroller).getHeight() - actionBarHeight;
        final float alpha = Math.min(firstItemHeight, Math.max(0, top)) / firstItemHeight;
        mIntroView.setTranslationY(-firstItemHeight / 3.0f * alpha);
        View decorView = getWindow().getDecorView();
        removeOverdraw(decorView, alpha);
        ViewGroup container = (ViewGroup) source.findViewById(R.id.container);
        final int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            View item = container.getChildAt(i);
            View v = item.findViewById(R.id.state);
            if (v != null && v.getGlobalVisibleRect(mTempRect)) {
                ((StateView) v).reveal(source, item.getBottom());
            }
        }
    }

    private void loadPhotos() {

        /**
         * 这里的异步加载用的比较好，如果直接执行 finishLoadingPhotos 会导致页面渲染比较慢
         * 还会覆盖掉部分 view
         */

        final Resources resources = getResources();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (State s : mStates) {
                    for (int resId : s.photos) {
                        s.bitmaps.add(BitmapFactory.decodeResource(resources, resId));
                    }
                }

                mIntroView.post(new Runnable() {
                    @Override
                    public void run() {
                        finishLoadingPhotos();
                    }
                });
            }
        }, "Photos Loader").start();
    }

    private void finishLoadingPhotos() {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = getLayoutInflater();

        /**
         * Space 这里让 FrameLayout 第一帧图片暴露出来，但是没有不能设置点击效果
         */
        Space spacer = new Space(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, findViewById(R.id.scroller).getHeight()));
        container.addView(spacer);

        for (State s : mStates) {
            addState(inflater, container, s);
        }
    }

    private void addState(LayoutInflater inflater, LinearLayout container, final State state) {
        final int margin = getResources().getDimensionPixelSize(R.dimen.activity_peek_margin);

        final View view = inflater.inflate(R.layout.item_state, container, false);
        final View stateView = view.findViewById(R.id.state);
        view.setBackgroundResource(state.background);
        stateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SK", "======");
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SK", "----");
            }
        });

        LinearLayout subContainer = (LinearLayout) view.findViewById(R.id.sub_container);
        Space spacer = new Space(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(container.getWidth() - margin, LinearLayout.LayoutParams.MATCH_PARENT));
        subContainer.addView(spacer);

        ImageView first = null;
        for (Bitmap b : state.bitmaps) {
            ImageView image = (ImageView) inflater.inflate(R.layout.item_photo, subContainer, false);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("SK", "======");
                }
            });
            if (first == null) first = image;
            image.setImageBitmap(b);
            subContainer.addView(image);
        }

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        first.setTag(cm);
        first.setColorFilter(new ColorMatrixColorFilter(cm));

        final ImageView bw = first;

        TrackingHorizontalScrollView s = (TrackingHorizontalScrollView) view.findViewById(R.id.scroller);
        s.setOnScrollChangedListener(new TrackingHorizontalScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(TrackingHorizontalScrollView source,
                                        int l, int t, int oldl, int oldt) {
                final float width = source.getWidth() - margin;
                final float alpha = Math.min(width, Math.max(0, l)) / width;

                stateView.setTranslationX(-width / 3.0f * alpha);
                removeStateOverdraw(view, state, alpha);

                if (alpha < 1.0f) {
                    ColorMatrix cm = (ColorMatrix) bw.getTag();
                    cm.setSaturation(alpha);
                    bw.setColorFilter(new ColorMatrixColorFilter(cm));
                } else {
                    bw.setColorFilter(null);
                }
            }
        });

        container.addView(view);
    }

    private void removeStateOverdraw(View stateView, State state, float alpha) {
        if (alpha >= 1.0f && stateView.getBackground() != null) {
            stateView.setBackground(null);
            stateView.findViewById(R.id.state).setVisibility(View.INVISIBLE);
        } else if (alpha < 1.0f && stateView.getBackground() == null) {
            stateView.setBackgroundResource(state.background);
            stateView.findViewById(R.id.state).setVisibility(View.VISIBLE);
        }
    }

    private void removeOverdraw(View decorView, float alpha) {
        if (alpha >= 1.0f) {
            // Note: setting a large negative translation Y to move the View
            // outside of the screen is an optimization. We could make the view
            // invisible/visible instead but this would destroy/create its backing
            // layer every time we toggle visibility. Since creating a layer can
            // be expensive, especially software layers, we would introduce stutter
            // when the view is made visible again.
            mIntroView.setTranslationY(-mIntroView.getHeight() * 2.0f);
        }
        if (alpha >= 1.0f && decorView.getBackground() != null) {
            mWindowBackground = decorView.getBackground();
            decorView.setBackground(null);
        } else if (alpha < 1.0f && decorView.getBackground() == null) {
            decorView.setBackground(mWindowBackground);
            mWindowBackground = null;
        }
    }
}
