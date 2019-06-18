package com.example.smartfarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OnOffListAdapter extends BaseAdapter {
    private int nListCnt = 0;
    private ArrayList<OnOffItemData> m_itemDataList = null;
    LayoutInflater inflater = null;

    public OnOffListAdapter(ArrayList<OnOffItemData> itemData){
        m_itemDataList = itemData;
        nListCnt = m_itemDataList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.onoff, parent, false);
        }

        TextView sensor = (TextView) convertView.findViewById(R.id.onoff_sensor_name);
        ToggleButton toggleBnt = (ToggleButton) convertView.findViewById(R.id.onfoff_toggle);

        sensor.setText(m_itemDataList.get(position).name);
        if(m_itemDataList.get(position).value.equals("0"))
            toggleBnt.setChecked(false);
        else
            toggleBnt.setChecked(true);

        return convertView;
    }
}
