package com.sunkai.lab.androidlab.diybanner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sunkai on 2017/6/14.
 */

public class MyPageAdapter extends PagerAdapter {
    private List<TextView> longList;
    private ViewPager viewPager;

    public MyPageAdapter(List<TextView> longList, ViewPager viewPager) {
        this.longList = longList;
        this.viewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.setPageTransformer(false, new CustomTransformer());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(getCount() / 2);
    }

    @Override
    public int getCount() {
        return null == longList ? 0 : longList.size() * 100;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % longList.size();
        if (longList.get(position).getParent() != null) {
            container.removeView(longList.get(position));
        }
        container.addView(longList.get(position));
        return longList.get(position);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = getCount() / 2;
            viewPager.setCurrentItem(position, true);
        } else if (position == getCount() - 1) {
            position = getCount() / 2;
            viewPager.setCurrentItem(position, true);
        }
    }
}
