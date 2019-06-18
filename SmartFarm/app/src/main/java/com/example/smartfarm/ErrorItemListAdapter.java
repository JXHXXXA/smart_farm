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
    private ArrayList<ErrorItemData> m_oData = null;
    private int nListCnt = 0;

    public ErrorItemListAdapter(ArrayList<ErrorItemData> _oData) {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        TextView sensor_name = (TextView) convertView.findViewById(R.id.error_sensor_name);
        TextView type = (TextView) convertView.findViewById(R.id.error_type);
        TextView occur_date = (TextView) convertView.findViewById(R.id.error_occur_date);
        TextView recover_date = (TextView) convertView.findViewById(R.id.error_recover_date);

        sensor_name.setText(m_oData.get(position).sensor_name);
        type.setText(m_oData.get(position).type);
        occur_date.setText(m_oData.get(position).occur_date);
        recover_date.setText(m_oData.get(position).recover_date);

        return convertView;
    }
}
