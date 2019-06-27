package com.example.smartfarm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    int selectedData;
    public DatePickerFragment(){}

    @SuppressLint("ValidFragment")
    public DatePickerFragment(int selectedData){
        this.selectedData = selectedData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();      // 오늘 날짜로 디폴트값을 설정하기 위해 캘린더 객체 선언
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);              // MONTH : 0~11
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day); // this는 리스너를 가르키는데 이 프래그먼트 클래스 자신을 가리킨다.
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        DashBoardActivity activity = (DashBoardActivity) getActivity();  // MainActivity의 birthday 버튼에 접근하기 위해 액티비티 객체 선언
        String _month, _day;
        if(month+1 < 10){
            _month = "0" + (month+1);
        } else{
            _month = "" + (month+1);
        }

        if(day < 10){
            _day = "0" + day;
        } else{
            _day = "" + day;
        }

        if (selectedData==0){
            activity.startDate.setText(year+"/"+(month+1)+"/"+ day); // 유저가 선택한 날짜로 버튼 텍스트 변경
            activity.startDate.setId(Integer.parseInt(year+""+ _month + _day));
        } else if (selectedData==1){
            activity.endDate.setText(year+"/"+(month+1)+"/"+ day); // 유저가 선택한 날짜로 버튼 텍스트 변경
            activity.endDate.setId(Integer.parseInt(year+""+ _month + _day));
        } else if (selectedData==2){
            activity.statisticsStartDate.setText(year+"/"+(month+1)+"/"+ day); // 유저가 선택한 날짜로 버튼 텍스트 변경
            activity.statisticsStartDate.setId(Integer.parseInt(year+""+ _month + _day));
        } else if (selectedData==3){
            activity.statisticsEndDate.setText(year+"/"+(month+1)+"/"+ day); // 유저가 선택한 날짜로 버튼 텍스트 변경
            activity.statisticsEndDate.setId(Integer.parseInt(year+""+ _month + _day));
        }
    }

}