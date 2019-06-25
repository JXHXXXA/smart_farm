package com.example.smartfarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OnOffListAdapter extends BaseAdapter {
    private int nListCnt = 0;
    private ArrayList<OnOffItemData> m_itemDataList = null;
    LayoutInflater inflater = null;
    ToggleButton toggleBnt;
    TextView sensor;

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
        return nListCnt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
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

        sensor = (TextView) convertView.findViewById(R.id.onoff_sensor_name);
        toggleBnt = (ToggleButton) convertView.findViewById(R.id.onfoff_toggle);
        toggleBnt.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleBnt.isChecked()) {
                    m_itemDataList.get(position).setValue("0");
                    toggleBnt.setChecked(false);
                    System.out.println(m_itemDataList.get(position).name+" 이(가) Off 되었습니다.");
                }
                else {
                    m_itemDataList.get(position).setValue("1");
                    toggleBnt.setChecked(true);
                    System.out.println(m_itemDataList.get(position).name+" 이(가) On 되었습니다.");
                }
            }
        });

        sensor.setText(m_itemDataList.get(position).name);

        if(m_itemDataList.get(position).value.equals("0"))
            toggleBnt.setChecked(false);
        else
            toggleBnt.setChecked(true);

        return convertView;
    }
}
