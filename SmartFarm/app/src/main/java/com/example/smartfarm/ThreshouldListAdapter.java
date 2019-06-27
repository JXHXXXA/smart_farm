package com.example.smartfarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class ThreshouldListAdapter extends BaseAdapter {
    private ArrayList<ThresholdItemData> listViewItemList;
    private ArrayList<ThresholdItemData> filteredItemList;
    public static String id,name,minValue,maxValue,time_stamp;

    public ThreshouldListAdapter(ArrayList<ThresholdItemData> listViewItemList) {
        this.listViewItemList = listViewItemList;
        filteredItemList = this.listViewItemList;
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final ThresholdViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new ThresholdViewHolder();
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.threshold, parent, false);
            holder.sensor_name = (TextView) convertView.findViewById(R.id.threshold_sensor_name);
            holder.min = (EditText)convertView.findViewById(R.id.threshold_edit_min);
            holder.max = (EditText)convertView.findViewById(R.id.threshold_edit_max);

            convertView.setTag(holder);
        } else {
            holder = (ThresholdViewHolder)convertView.getTag();
        }

        holder.ref = position;

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final TextView sensor_name = (TextView) convertView.findViewById(R.id.threshold_sensor_name);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        final ThresholdItemData listViewItem = filteredItemList.get(position);

        holder.sensor_name.setText(listViewItem.getName());
        holder.min.setText(listViewItem.getMinValue());
        holder.max.setText(listViewItem.getMaxValue());

        name = holder.sensor_name.getText().toString();
        minValue = holder.min.getText().toString();
        maxValue = holder.max.getText().toString();

        holder.min.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filteredItemList.get(holder.ref).setMinValue(s.toString());
            }
        });

        holder.max.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filteredItemList.get(holder.ref).setMaxValue(s.toString());
            }
        });

        return convertView;
    }

    public Object getItemMinValue(int position) {
        return filteredItemList.get(position).minValue;
    }
    public Object getItemMaxValue(int position) {
        return filteredItemList.get(position).maxValue;
    }
}
