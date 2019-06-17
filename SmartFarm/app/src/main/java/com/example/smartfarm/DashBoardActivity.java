package com.example.smartfarm;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DashBoardActivity extends AppCompatActivity {
    String area_id;
    TextView area_name;
    String sensor_id, value, time_stamp;

    ImageView backImg;

    float[] sensor_values;

    Button mButton;
    GridLayout gl;
    GridLayout.LayoutParams params;
    TextView time;
    ListView onoffView;
    ListView thresholdView;
    ListView settingView;

    private LineChart lineChart;

    ImageView imgView;
    FrameLayout frame;
    Button button1;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Intent intent = getIntent();
        area_id = intent.getStringExtra("area_id");
        System.out.println("area_id : " + area_id);

        sensor_values = new float[23];

        area_name = (TextView) findViewById(R.id.area_name);
        area_name.setText("코끼리 하마 농장 "+ (Integer.parseInt(area_id)+1) +"동");

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


        /* here ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
        setSensorValueList();
        getTime();
        chart();
        setButtonSeleted();
        setOnOff();
        setThreshold();
        setSetting();
        /* 장치설정의 3가지 view */
        onoffView = (ListView) findViewById(R.id.onoff);
        thresholdView = (ListView) findViewById(R.id.threshold);
        settingView = (ListView) findViewById(R.id.setting);

        frame = (FrameLayout) findViewById(R.id.frame);
        frame.removeView(thresholdView);
        frame.removeView(settingView);
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);
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

    private void getSensorValue(){
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


    private void chart() {
        lineChart = (LineChart)findViewById(R.id.chart);
        lineChart.invalidate();
        lineChart.clear();

        /*
        -------------------------------------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------------------------------
        */
//        ArrayList<Entry> values = new ArrayList<>();//차트 데이터 셋에 담겨질 데이터

//        for (Record record : records) { //values에 데이터를 담는 과정
//            long dateTime = record.getDateTime();
//            float weight = (float) record.getWeight();
//            values.add(new Entry(dateTime, weight));
//        }

        /*
        -------------------------------------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------------------------------
        */

        ArrayList<Entry> entries = new ArrayList<>();

        Date now = new Date();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        System.out.println(timeStampFormat.format(now));
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateTimeFormat.format(now));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.println(timeFormat.format(now));

        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));

        LineDataSet lineDataSet = new LineDataSet(entries, "측정값");
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

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

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
        lineChart.invalidate();
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
}
