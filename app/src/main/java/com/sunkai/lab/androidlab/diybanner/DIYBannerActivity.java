package com.sunkai.lab.androidlab.diybanner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunkai.lab.androidlab.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkai on 2017/6/14.
 */

public class DIYBannerActivity extends Activity {
    private ViewPager viewPager;
    private LinearLayout indicatorContainer;
    private Handler mHandler = new Handler();
    private ArrayList<ImageView> indicators = new ArrayList<>();
    private int[] indicatorRes = new int[]{R.drawable.indicator_normal, R.drawable.indicator_selected};
    List<TextView> list;
    private int delayedTime = 1000;
    private boolean isAutoPlay = true;

    private final Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                int mCurrentItem = viewPager.getCurrentItem();
                mCurrentItem++;
                viewPager.setCurrentItem(mCurrentItem);
                mHandler.postDelayed(this, delayedTime);

            } else {
                mHandler.postDelayed(this, delayedTime);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_banner_layout);

        initView();
        initData();
        initEvent();
        mHandler.postDelayed(loopRunnable, delayedTime);

    }

    private void initEvent() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int realSelectPosition = position % indicators.size();
                for (int i = 0; i < list.size(); i++) {
                    if (i == realSelectPosition) {
                        indicators.get(i).setImageResource(indicatorRes[1]);
                    } else {
                        indicators.get(i).setImageResource(indicatorRes[0]);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;

                }
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        addData(list);
        new MyPageAdapter(list, viewPager);
        initIndicator();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        indicatorContainer = (LinearLayout) findViewById(R.id.banner_indicator_container);
    }

    private void addData(List<TextView> list) {
        TextView textView = new TextView(getApplicationContext());
        textView.setBackgroundColor(0xff00ff00);
        list.add(textView);
        textView = new TextView(getApplicationContext());
        textView.setBackgroundColor(0xffff0000);
        list.add(textView);

        textView = new TextView(getApplicationContext());
        textView.setBackgroundColor(0xff0000ff);
        list.add(textView);

        textView = new TextView(getApplicationContext());
        textView.setBackgroundColor(0xff000000);
        list.add(textView);

        textView = new TextView(getApplicationContext());
        textView.setBackgroundColor(0xffffffff);
        list.add(textView);
    }

    private void initIndicator() {
        indicatorContainer.removeAllViews();
        indicators.clear();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setPadding(6, 0, 6, 0);
            if (i == (viewPager.getCurrentItem() % list.size())) {
                imageView.setImageResource(indicatorRes[1]);
            } else {
                imageView.setImageResource(indicatorRes[0]);
            }

            indicators.add(imageView);
            indicatorContainer.addView(imageView);
        }
    }
}
