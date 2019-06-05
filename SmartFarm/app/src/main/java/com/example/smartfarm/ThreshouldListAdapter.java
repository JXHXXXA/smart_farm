package com.example.smartfarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ThreshouldListAdapter extends BaseAdapter {
    private int nListCnt = 0;
    private ArrayList<ThresholdItemData> m_itemDataList = null;
    LayoutInflater inflater = null;

    public ThreshouldListAdapter(ArrayList<ThresholdItemData> itemData) {
        m_itemDataList = itemData;
        nListCnt = m_itemDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.threshold, parent, false);
        }

        TextView sensor = (TextView) convertView.findViewById(R.id.threshold_sensor_name);
        EditText thresholdEditMin = (EditText) convertView.findViewById(R.id.threshold_edit_min);
        EditText thresholdEditMax = (EditText) convertView.findViewById(R.id.threshold_edit_max);

        sensor.setText(m_itemDataList.get(position).sensorName);
        thresholdEditMin.setText(m_itemDataList.get(position).minValue);
        thresholdEditMax.setText(m_itemDataList.get(position).maxValue);
        return convertView;
    }
}
