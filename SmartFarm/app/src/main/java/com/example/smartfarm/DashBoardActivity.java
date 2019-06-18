package com.example.smartfarm;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DashBoardActivity extends AppCompatActivity {
    String area_id;
    TextView area_name;
    String sensor_id, value, time_stamp;

    ArrayList<Entry> sensor_entry;

    ImageView backImg;

    float[] sensor_values;

    long reference_timestamp;

    Button mButton;
    GridLayout gl;
    GridLayout.LayoutParams params;
    TextView time;
    ListView onoffView;
    ListView thresholdView;
    ListView settingView;
    ListView errorView;
    private LineChart lineChart;
    TextView start_date;
    TextView end_date;
    Date currentDate;
    int iYear,iMonth,iDay;


    ImageView imgView;
    FrameLayout frame;
    Button button1;
    Button button2;
    Button button3;
    Button startDate, endDate, error_search;
    Spinner spinner;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Intent intent = getIntent();
        area_id = intent.getStringExtra("area_id");
        System.out.println("area_id : " + area_id);

        sensor_values = new float[23];

        area_name = (TextView) findViewById(R.id.area_name);
        area_name.setText("코끼리 하마 농장 "+ area_id +"동");

        backImg = (ImageView) findViewById(R.id.dash_board_back);
        backImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        final TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("현재값");
        tabHost1.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        final TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("장치설정");
        tabHost1.addTab(ts2);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        final TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("통계");
        tabHost1.addTab(ts3);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 4"), (페이지 뷰:"content4")
        final TabHost.TabSpec ts4 = tabHost1.newTabSpec("Tab4");
        ts4.setContent(R.id.content4);
        ts4.setIndicator("오류");
        tabHost1.addTab(ts4);

        imgView = (ImageView) findViewById(R.id.verified);
        tabHost1.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (ts1.getTag().equals(tabId)) {
                    imgView.setVisibility(View.INVISIBLE);
                }

                if (ts2.getTag().equals(tabId)) {
                    imgView.setVisibility(View.VISIBLE);
                }

                if (ts3.getTag().equals(tabId)) {
                    imgView.setVisibility(View.VISIBLE);
                }

                if (ts4.getTag().equals(tabId)) {
                    imgView.setVisibility(View.VISIBLE);
                }
            }
        });

        imgView.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("헤헤");
            }
        });

        sensor_entry = new ArrayList<>();

        /* here ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
        setSensorValueList();
        getTime();

        setButtonSeleted();
        setOnOff();
        setThreshold();
        setSetting();
        setSpinner();


        /* 장치설정의 3가지 view */
        onoffView = (ListView) findViewById(R.id.onoff);
        thresholdView = (ListView) findViewById(R.id.threshold);
        settingView = (ListView) findViewById(R.id.setting);
/*        errorView = (ListView) findViewByld(R.id.error);*/
        frame = (FrameLayout) findViewById(R.id.frame);
        frame.removeView(thresholdView);
        frame.removeView(settingView);
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);
        startDate = (Button) findViewById(R.id.start_date);
        endDate = (Button) findViewById(R.id.end_date);
        error_search = (Button) findViewById(R.id.error_search);


        /* 에러 날짜 */
        Date today = new Date();      // birthday 버튼의 초기화를 위해 date 객체와 SimpleDataFormat 사용
        SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy/MM/dd");
        String result = dateFormat.format(today);
        startDate.setText(result);       // 오늘 날짜로 birthday 버튼 텍스트 초기화
        endDate.setText(result);
        error_search.setOnClickListener(new View.OnClickListener() {        // 저장 버튼을 클릭하면 토스트로 고객 정보를 띄워주기
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), startDate.getText().toString() + "\n" + endDate.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        // 통계 서버연동
        getSensorValue(13, "20190609", "humidity");
    }

    public void onStartDateClicked(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment(0);   //DatePickerFragment 객체 생성
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }

    public void onEndDateClicked(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment(1);   //DatePickerFragment 객체 생성
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }
/*
    public void onText3Clicked(View v) {

        String strDate = start_date.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try{
            Date pickDate = new Date(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(pickDate);
            Dialog dia = null;
            //strDate값을 기본값으로 날짜 선택 다이얼로그 생성
            dia =new DatePickerDialog(getApplicationContext(), dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            dia.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/


    private void setSpinner(){
        arrayAdapter = arrayAdapter.createFromResource(getApplicationContext(),R.array.city,R.layout.spinner_color);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), adapterView.getSelectedItem().toString()+"(이)가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void setSensorValueList(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray farmList = jsonResponse.getJSONArray("data");

                    for(int i=0; i<farmList.length(); i++) {
                        sensor_id = farmList.getJSONObject(i).getString("sensor_id");
                        value = farmList.getJSONObject(i).getString("value");
                        time_stamp = farmList.getJSONObject(i).getString("time_stamp");

                        getSensorValue(i, Float.parseFloat(value));
                        sensor_values[Integer.parseInt(sensor_id)-1] = Float.parseFloat(value);
                        System.out.println("sensor_id : " + (Integer.parseInt(sensor_id)) + " , value : " + sensor_values[Integer.parseInt(sensor_id)-1]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://uxilt2y0g6.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/sensors";
        AreaListRequest listRequest = new AreaListRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(listRequest);
    }

    private void getSensorValue(int sensorId, String searchDate, String sensorName){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray sensorValueList = jsonResponse.getJSONArray("data");

                    reference_timestamp = 0;
                    for(int i=0; i<sensorValueList.length(); i++) {
                        String strTime = sensorValueList.getJSONObject(i).getString("time_stamp");
                        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date timeStamp = timeStampFormat.parse(strTime);
                        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeStamp);
                        timeStamp = dateTimeFormat.parse(dateTime);

                        if(i==0){
                            reference_timestamp = timeStamp.getTime();
                        }

                        sensor_entry.add(new Entry(
                                timeStamp.getTime() - reference_timestamp,
                                Float.parseFloat(sensorValueList.getJSONObject(i).getString("value"))
                                )
                        );

                        System.out.println("data : " + sensor_entry.get(i));
                        System.out.println("datetime : " + timeStamp.getTime());
                        System.out.println("value : " + Float.parseFloat(sensorValueList.getJSONObject(i).getString("value")));
                    }
                    chart();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://uxilt2y0g6.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/sensors/"+sensorId+"/"+searchDate+"?sensorName="+sensorName;
        SensorValueRequest sensorValueRequest = new SensorValueRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(sensorValueRequest);
    }

    private void chart() {
        lineChart = (LineChart)findViewById(R.id.chart);
        lineChart.invalidate();
        lineChart.clear();

        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));

//        for(int i=0; i<sensor_entry.size(); i++){
//            System.out.println(sensor_entry.get(i));
//        }

        //Collections.sort(sensor_entry, new EntryXComparator());
        LineDataSet lineDataSet = new LineDataSet(sensor_entry, "측정값");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF0ed60a"));
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FF0ed60a"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        SensorMarkerView marker = new SensorMarkerView(this,R.layout.custom_marker_view);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);

        lineChart.setData(lineData);
    }

    /* 이게 set이라기 보다는 onoff 화면을 띄어주는건데.. 여기에 클릭할 시 이벤트를 추가해야할 걱 같아용
       일단은... 화면 만들어 뒀어요*/
    private void setOnOff(){
        int nDatCnt=0;
        ArrayList<OnOffItemData> onoffData = new ArrayList<>();
        for (int i=0; i<10; i++)
        {
            OnOffItemData onOffItem = new OnOffItemData();
            onOffItem.setSensor = "센서 " + (i+1);
            onoffData.add(onOffItem);
        }

        onoffView = (ListView)findViewById(R.id.onoff);
        ListAdapter onOFFAdapter = new OnOffListAdapter(onoffData);
        onoffView.setAdapter(onOFFAdapter);
    }

    private void setThreshold(){
        int nDatCnt=0;
        ArrayList<ThresholdItemData> thresholdData = new ArrayList<>();
        for (int i=0; i<10; i++)
        {
            ThresholdItemData thresholdItem = new ThresholdItemData();
            thresholdItem.sensorName = "센서 " + (i+1);
            thresholdItem.maxValue = "1";
            thresholdItem.minValue = "1";
            thresholdData.add(thresholdItem);
        }

        thresholdView = (ListView)findViewById(R.id.threshold);
        ListAdapter threshouldAdapter = new ThreshouldListAdapter(thresholdData);
        thresholdView.setAdapter(threshouldAdapter);

    }

    private void setSetting(){
        int nDatCnt=0;
        ArrayList<SettingItemData> settingData = new ArrayList<>();
        for (int i=0; i<10; i++)
        {
            SettingItemData settingItem = new SettingItemData();
            settingItem.sensorName = "센서 " + (i+1);
            settingItem.editValue = "1";
            settingData.add(settingItem);
        }

        settingView = (ListView)findViewById(R.id.setting);
        ListAdapter settingAdapter = new SettingListAdapter(settingData);
        settingView.setAdapter(settingAdapter);

    }


    /* 장치설정 전환 */
    private void setButtonSeleted() {
        button1 = (Button) findViewById(R.id.dash_board_onoff);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setSelected(true);
                button2.setSelected(false);
                button3.setSelected(false);
                changeView(0);
            }
        });

        button2 = (Button) findViewById(R.id.dash_board_threshold);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setSelected(false);
                button2.setSelected(true);
                button3.setSelected(false);
                changeView(1);
            }
        });

        button3 = (Button) findViewById(R.id.dash_board_setting);
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setSelected(false);
                button2.setSelected(false);
                button3.setSelected(true);
                changeView(2);
            }
        });
    }

    private void changeView(int index) {
        // 0 번째 뷰 제거. (뷰가 하나이므로, 0 번째 뷰를 제거하면 모든 뷰가 제거됨.)
        frame.removeViewAt(0);

        // index에 해당하는 textView 표시
        switch (index) {
            case 0:
                frame.addView(onoffView);
                break;
            case 1:
                frame.addView(thresholdView);
                break;
            case 2:
                frame.addView(settingView);
                break;
        }
    }


    private void getTime() {
        /* 일단 현재 시간 받아오는거로..*/
        time = (TextView) findViewById(R.id.dash_board_time);
        time.setText(DateUtils.formatDateTime(getBaseContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE));
    }

    private void getSensorValue(int i, float value) {
        /* ---------------------------Grid-------------------------- */
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        gl = (GridLayout) findViewById(R.id.dash_board_grid);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float dp = 10f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);

//        System.out.println("dm : " + dm.density);
//        System.out.println("width : " + width);
        // areaButton.setId(Integer.parseInt("area_"+num));
        // areaButton.setOnClickListener(mOnClick); //버튼에 OnClickListener를 지정(OnClickListener)

        mButton = new Button(this);
//            areaButton.setId(@+id/area_bt);
        mButton.setWidth((width - pixels * 5) / 4);
        mButton.setHeight((width - pixels * 5) / 4);
        mButton.setText("센서 " + Integer.toString(i + 1)); //버튼에 들어갈 텍스트를 지정(String)
        mButton.getBackground().setColorFilter(Color.parseColor("#b5ddc0"), PorterDuff.Mode.DARKEN);
        params = new GridLayout.LayoutParams();

        if ((i + 1) % 4 != 0) {
            params.setMargins(0, 0, pixels, pixels);
        } else {
            params.setMargins(0, 0, 0, pixels);
        }

        mButton.setId(i);

        final int position = i + 1;
        gl.addView(mButton, i, params);

    }

    private void setData(int count, float range) {
    }

    private class HourAxisValueFormatter implements IAxisValueFormatter {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Date valueDate = (new Date((long) value));
            return dateTimeFormat.format(valueDate);
        }
    }
}
