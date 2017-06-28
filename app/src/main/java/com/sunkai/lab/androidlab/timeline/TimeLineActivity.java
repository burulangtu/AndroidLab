package com.sunkai.lab.androidlab.timeline;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.sunkai.lab.androidlab.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeLineActivity extends Activity {

    RecyclerView recyclerView;
    TimeLineAdapter timeLineAdapter;
    DotItemDecoration dotItemDecoration;
    List<Event> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        initData();
        initView();

    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new Event(1497229200, "事件1"));
        list.add(new Event(1497229200, "事件2"));
        list.add(new Event(1497240000, "事件3"));
        list.add(new Event(1497229200, "事件4"));
        list.add(new Event(1497247200, "事件5"));
        list.add(new Event(1497229200, "事件6"));
        list.add(new Event(1497229200, "事件7"));
        list.add(new Event(1497229200, "事件8"));
        list.add(new Event(1497252600, "事件8"));


        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                if (event.time > t1.time) {
                    return 1;
                } else if (event.time < t1.time) {
                    return -1;
                }
                return 0;
            }
        });
        timeLineAdapter = new TimeLineAdapter(list);
        dotItemDecoration = new DotItemDecoration();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(timeLineAdapter);
        recyclerView.addItemDecoration(dotItemDecoration);
    }
}
