package com.sunkai.lab.androidlab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkai on 2017/7/21.
 */

public class LabListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> labItemTitles = new ArrayList<>();

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public LabListAdapter(List<String> labItemTitles) {
        this.labItemTitles = labItemTitles;
    }

    private View.OnClickListener onClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_item, null, false);
        return new LabItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((TextView) holder.itemView.findViewById(R.id.lab_text)).setText(labItemTitles.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    view.setTag(position);
                    onClickListener.onClick(view);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return labItemTitles == null ? 0 : labItemTitles.size();
    }


    private static class LabItemHolder extends RecyclerView.ViewHolder {

        public LabItemHolder(View itemView) {
            super(itemView);
        }
    }

}
