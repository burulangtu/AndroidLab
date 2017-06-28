package com.sunkai.lab.androidlab.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunkai.lab.androidlab.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sunkai on 2017/6/28.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    List<Event> data;
    int[] colors = {0xffFFAD6C, 0xff62f434, 0xffdeda78, 0xff7EDCFF, 0xff58fdea, 0xfffdc75f};//颜色组

    public TimeLineAdapter(List<Event> data) {
        this.data = data;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_line_item, parent, false);
        return new TimeLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        holder.time.setText(LongtoStringFormat(1000 * data.get(position).getTime()));
        holder.textView.setText(data.get(position).getEvent());
        holder.time.setTextColor(colors[position % colors.length]);
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    static class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView textView;

        public TimeLineViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    /**
     * long转成2015.01.03格式
     *
     * @param time 单位ms s的话需要*1000
     * @return
     */
    public static String LongtoStringFormat(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd a HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
