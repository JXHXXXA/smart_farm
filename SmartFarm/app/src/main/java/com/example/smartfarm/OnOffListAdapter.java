package com.example.smartfarm;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OnOffListAdapter extends BaseAdapter {
    private ArrayList<OnOffItemData> listViewItemList;
    private ArrayList<OnOffItemData> filteredItemList;
    public static String name,onOff;

    public OnOffListAdapter(ArrayList<OnOffItemData> listViewItemList){
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
        final OnOffViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new OnOffViewHolder();
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.onoff, parent, false);
            holder.onOff_name = (TextView) convertView.findViewById(R.id.onoff_sensor_name);
            holder.onOff = (ToggleButton) convertView.findViewById(R.id.onfoff_toggle);

            convertView.setTag(holder);
        } else {
            holder = (OnOffViewHolder)convertView.getTag();
        }

        holder.ref = position;

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        final OnOffItemData listViewItem = filteredItemList.get(position);

        holder.onOff_name.setText(listViewItem.getName());
        if(listViewItem.getValue().equals("0"))
            holder.onOff.setChecked(false);
        else
            holder.onOff.setChecked(true);

        name = holder.onOff_name.getText().toString();
        if(holder.onOff.isChecked())
            onOff = "1";
        else
            onOff = "0";

        holder.onOff.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(holder.onOff.isChecked()) {
                    filteredItemList.get(holder.ref).setValue("1");
                    System.out.println(filteredItemList.get(holder.ref).name+" 이(가) On 되었습니다.");
                }
                else {
                    filteredItemList.get(holder.ref).setValue("0");
                    System.out.println(filteredItemList.get(holder.ref).name+" 이(가) Off 되었습니다.");
                }
            }
        });

        return convertView;

        /*
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
        */
    }

    public Object getItemValue(int position) {
        return filteredItemList.get(position).value;
    }
}
