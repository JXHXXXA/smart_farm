package com.example.smartfarm;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingListAdapter extends BaseAdapter {
    private ArrayList<SettingItemData> listViewItemList;
    private ArrayList<SettingItemData> filteredItemList;
    public static String id,name,value,time_stamp;

    public SettingListAdapter(ArrayList<SettingItemData> listViewItemList) {
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
        final SettingViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new SettingViewHolder();
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.setting, parent, false);
            holder.setting_name = (TextView) convertView.findViewById(R.id.setting_sensor_name);
            holder.value = (EditText) convertView.findViewById(R.id.setting_edit_value);

            convertView.setTag(holder);
        } else {
            holder = (SettingViewHolder)convertView.getTag();
        }

        holder.ref = position;

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        final SettingItemData listViewItem = filteredItemList.get(position);

        holder.setting_name.setText(listViewItem.getName());
        holder.value.setText(listViewItem.getValue());

        name = holder.setting_name.getText().toString();
        value = holder.value.getText().toString();

        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filteredItemList.get(holder.ref).setValue(s.toString());
            }
        });

        return convertView;
    }

    public Object getItemValue(int position) {
        return filteredItemList.get(position).value;
    }
}
