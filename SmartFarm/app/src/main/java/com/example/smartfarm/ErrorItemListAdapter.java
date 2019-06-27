package com.example.smartfarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ErrorItemListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<ErrorItemData> m_oData;
    private int nListCnt;

    public ErrorItemListAdapter(ArrayList<ErrorItemData> _oData) {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount() {
        return nListCnt;
    }

    @Override
    public Object getItem(int position) {
        return m_oData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.error, parent, false);
        }

        TextView sensor_type = (TextView) convertView.findViewById(R.id.error_sensor_name);
        TextView error_info = (TextView) convertView.findViewById(R.id.error_type);
        TextView occur_date = (TextView) convertView.findViewById(R.id.error_occur_date);
        TextView recover_date = (TextView) convertView.findViewById(R.id.error_recover_date);

        sensor_type.setText(m_oData.get(position).sensor_type);
        error_info.setText(m_oData.get(position).error_info);
        occur_date.setText(m_oData.get(position).time_stamp);
        if(m_oData.get(position).repair_time!=null) {
            if (m_oData.get(position).repair_time.equals("null"))
                recover_date.setText(null);
            else
                recover_date.setText(m_oData.get(position).repair_time);
        }
        return convertView;
    }
}
