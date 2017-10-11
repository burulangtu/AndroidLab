package com.sunkai.lab.androidlab.pullrefreshView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.sunkai.lab.androidlab.R;

public class PullRefreshViewActivity extends Activity implements MyPullRefreshView.RefreshCallBack {

    MyPullRefreshView myPullRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_view);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());

        myPullRefreshView = (MyPullRefreshView) findViewById(R.id.pull_refresh_view);
        myPullRefreshView.setRefreshCallBack(this);

    }

    @Override
    public void upRefresh(Scroller scroller, int y) {
        scroller.startScroll(0, y, 0, -100);
        myPullRefreshView.invalidate();
    }

    @Override
    public void downLoad(Scroller scroller, int y) {
        scroller.startScroll(0, y, 0, 100);
        myPullRefreshView.invalidate();
    }

    private class MyAdapter extends RecyclerView.Adapter {
        private int[] color = new int[]{
                0xffff00ff, 0xff00ffff, 0xffffff00
        };

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PullRefreshViewActivity.this).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            holder.itemView.setBackgroundColor(color[position % color.length]);
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
