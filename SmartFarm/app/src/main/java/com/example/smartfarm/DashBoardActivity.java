package com.example.smartfarm;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DashBoardActivity extends AppCompatActivity {
    String area_id;
    TextView area_name;
    String sensor_id, value, time_stamp;

    ArrayList<Entry> sensor_entry;

    ImageView backImg;

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

    ImageView verify;
    FrameLayout frame;
    Button button1;
    Button button2;
    Button button3;

    Button statisticsStartDate, statisticsEndDate, statisticsSearch;
    String searchSensorName;
    int searchSensorId;

    Button startDate, endDate, error_search;
    Spinner spinner;
    ArrayAdapter arrayAdapter;

    ArrayList<OnOffItemData> onoffData;
    OnOffItemData onOffItem;
    OnOffListAdapter onOffAdapter;

    ArrayList<ThresholdItemData> thresholdData;
    ThresholdItemData thresholdItem;
    ThreshouldListAdapter threshouldAdapter;

    ArrayList<SettingItemData> settingData;
    SettingItemData settingItem;
    SettingListAdapter settingAdapter;

    ArrayList<ErrorItemData> errorData;
    ErrorItemData errorItem;
    ErrorItemListAdapter errorAdapter;

    String settingDatas;

    SimpleDateFormat timeStampFormat;
    SimpleDateFormat dateFormat, dateFormat2, timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Intent intent = getIntent();
        area_id = intent.getStringExtra("area_id");
        System.out.println("area_id : " + area_id);

        timeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        timeFormat = new SimpleDateFormat("HH:mm:ss");

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

        verify = (ImageView) findViewById(R.id.verified);
        tabHost1.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (ts1.getTag().equals(tabId)) {
                    verify.setVisibility(View.INVISIBLE);
                }

                if (ts2.getTag().equals(tabId)) {
                    verify.setVisibility(View.VISIBLE);
                    verify.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                setOnOffSettings();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "On/Off 제어가 설정되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                if (ts3.getTag().equals(tabId)) {
                    verify.setVisibility(View.INVISIBLE);
                }

                if (ts4.getTag().equals(tabId)) {
                    verify.setVisibility(View.INVISIBLE);
                }
            }
        });

        sensor_entry = new ArrayList<>();
        getSensorValueList();

        time = (TextView) findViewById(R.id.dash_board_time);

        setButtonSeleted();

        getOnOffSettings();
//        getThresholdSettings();
//        getValueSettings();

        setSpinner();

        /* 장치설정의 3가지 view */
        onoffView = (ListView) findViewById(R.id.onoff);
        thresholdView = (ListView) findViewById(R.id.threshold);
        settingView = (ListView) findViewById(R.id.setting);
        errorView = (ListView) findViewById(R.id.error_table);
        frame = (FrameLayout) findViewById(R.id.frame);
        frame.removeView(thresholdView);
        frame.removeView(settingView);
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);

        Date today = new Date();      // birthday 버튼의 초기화를 위해 date 객체와 SimpleDataFormat 사용
        String _today = dateFormat.format(today);
        String _today2 = dateFormat2.format(today);

        statisticsStartDate = (Button) findViewById(R.id.statistics_start_date);
        statisticsEndDate = (Button) findViewById(R.id.statistics_end_date);
        statisticsSearch = (Button) findViewById(R.id.statistics_search);

        statisticsStartDate.setText(_today);
        statisticsStartDate.setId(Integer.parseInt(_today2));

        statisticsEndDate.setText(_today);
        statisticsEndDate.setId(Integer.parseInt(_today2));

        statisticsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), statisticsStartDate.getText().toString() + "\n" + statisticsEndDate.getText().toString(), Toast.LENGTH_LONG).show();
                getSensorValue(searchSensorId, Integer.toString(statisticsStartDate.getId()), Integer.toString(statisticsEndDate.getId()), searchSensorName);
            }
        });

        startDate = (Button) findViewById(R.id.start_date);
        endDate = (Button) findViewById(R.id.end_date);
        error_search = (Button) findViewById(R.id.error_search);
//        setError();

        /* 에러 날짜 */
        startDate.setText(_today);
        startDate.setId(Integer.parseInt(_today2));

        endDate.setText(_today);
        endDate.setId(Integer.parseInt(_today2));

        error_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), startDate.getText().toString() + "\n" + endDate.getText().toString(), Toast.LENGTH_LONG).show();
                getErrors(Integer.toString(startDate.getId()), Integer.toString(endDate.getId()));
            }
        });
    }

    public void onStatisticsStartDateClicked(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment(2);   //DatePickerFragment 객체 생성
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }

    public void onStatisticsEndDateClicked(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment(3);   //DatePickerFragment 객체 생성
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }

    public void onStartDateClicked(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment(0);   //DatePickerFragment 객체 생성
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }

    public void onEndDateClicked(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment(1);   //DatePickerFragment 객체 생성
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }


    private void setError(){
        errorData = new ArrayList<>();
        for (int i=0; i<30; i++)
        {
            ErrorItemData errorItem = new ErrorItemData();
            errorItem.sensor_type = "실내온도2";
            errorItem.error_info = "임계치 초과";
            errorItem.time_stamp = "2019/06/15";
            errorItem.repair_time = null;
            errorData.add(errorItem);
        }

        errorView = (ListView)findViewById(R.id.error_table);
        errorAdapter = new ErrorItemListAdapter(errorData);
        errorView.setAdapter(errorAdapter);
        System.out.println("임의 에러 10개 추가");
//        잘리는거맞추깅
//        setListViewHeightBasedOnChildren(errorView);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setSpinner(){
        arrayAdapter = arrayAdapter.createFromResource(getApplicationContext(),R.array.sensor,R.layout.spinner_color);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_color);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), adapterView.getSelectedItem().toString()+"(이)가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equals("실내온도1")
                || adapterView.getSelectedItem().toString().equals("실내온도2")
                || adapterView.getSelectedItem().toString().equals("실내온도3")
                || adapterView.getSelectedItem().toString().equals("실내온도4")){
                    searchSensorName = "indoor_temperature";
                    if(adapterView.getSelectedItem().toString().equals("실내온도1"))
                        searchSensorId = 2;
                    else if(adapterView.getSelectedItem().toString().equals("실내온도2"))
                        searchSensorId = 3;
                    else if(adapterView.getSelectedItem().toString().equals("실내온도3"))
                        searchSensorId = 4;
                    else if(adapterView.getSelectedItem().toString().equals("실내온도4"))
                        searchSensorId = 5;
                }
                else if(adapterView.getSelectedItem().toString().equals("온수온도")){
                    searchSensorName = "water_temperature";
                    searchSensorId = 6;
                }
                else if(adapterView.getSelectedItem().toString().equals("이산화탄소1")
                    || adapterView.getSelectedItem().toString().equals("이산화탄소2")
                    || adapterView.getSelectedItem().toString().equals("이산화탄소3")
                    || adapterView.getSelectedItem().toString().equals("이산화탄소4")
                    || adapterView.getSelectedItem().toString().equals("이산화탄소5")
                    || adapterView.getSelectedItem().toString().equals("이산화탄소6")){
                    searchSensorName = "co2";
                    if(adapterView.getSelectedItem().toString().equals("이산화탄소1"))
                        searchSensorId = 8;
                    else if(adapterView.getSelectedItem().toString().equals("이산화탄소2"))
                        searchSensorId = 9;
                    else if(adapterView.getSelectedItem().toString().equals("이산화탄소3"))
                        searchSensorId = 10;
                    else if(adapterView.getSelectedItem().toString().equals("이산화탄소4"))
                        searchSensorId = 11;
                    else if(adapterView.getSelectedItem().toString().equals("이산화탄소5"))
                        searchSensorId = 12;
                    else if(adapterView.getSelectedItem().toString().equals("이산화탄소6"))
                        searchSensorId = 13;
                }
                else if(adapterView.getSelectedItem().toString().equals("습도1")
                    || adapterView.getSelectedItem().toString().equals("습도2")
                    || adapterView.getSelectedItem().toString().equals("습도3")
                    || adapterView.getSelectedItem().toString().equals("습도4")
                    || adapterView.getSelectedItem().toString().equals("습도5")
                    || adapterView.getSelectedItem().toString().equals("습도6")){
                    searchSensorName = "humidity";
                    if(adapterView.getSelectedItem().toString().equals("습도1"))
                        searchSensorId = 15;
                    else if(adapterView.getSelectedItem().toString().equals("습도2"))
                        searchSensorId = 16;
                    else if(adapterView.getSelectedItem().toString().equals("습도3"))
                        searchSensorId = 17;
                    else if(adapterView.getSelectedItem().toString().equals("습도4"))
                        searchSensorId = 18;
                    else if(adapterView.getSelectedItem().toString().equals("습도5"))
                        searchSensorId = 19;
                    else if(adapterView.getSelectedItem().toString().equals("습도6"))
                        searchSensorId = 20;
                }
                else if(adapterView.getSelectedItem().toString().equals("전자저울1")
                    || adapterView.getSelectedItem().toString().equals("전자저울2")
                    || adapterView.getSelectedItem().toString().equals("전자저울3")
                    || adapterView.getSelectedItem().toString().equals("전자저울4")
                    || adapterView.getSelectedItem().toString().equals("전자저울5")
                    || adapterView.getSelectedItem().toString().equals("전자저울6")){
                    searchSensorName = "scale";
                    if(adapterView.getSelectedItem().toString().equals("전자저울1"))
                        searchSensorId = 21;
                    else if(adapterView.getSelectedItem().toString().equals("전자저울2"))
                        searchSensorId = 22;
                    else if(adapterView.getSelectedItem().toString().equals("전자저울3"))
                        searchSensorId = 23;
                    else if(adapterView.getSelectedItem().toString().equals("전자저울4"))
                        searchSensorId = 24;
                    else if(adapterView.getSelectedItem().toString().equals("전자저울5"))
                        searchSensorId = 25;
                    else if(adapterView.getSelectedItem().toString().equals("전자저울6"))
                        searchSensorId = 26;
                }
                else if(adapterView.getSelectedItem().toString().equals("조명")){
                    searchSensorName = "illuminance";
                    searchSensorId = 27;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void getSensorValueList(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray farmList = jsonResponse.getJSONArray("data");

                    int count = 0;
                    for(int i=0; i<farmList.length(); i++) {
                        sensor_id = farmList.getJSONObject(i).getString("sensor_id");
                        value = farmList.getJSONObject(i).getString("value");
                        time_stamp = farmList.getJSONObject(i).getString("time_stamp");

                        if(SensorType.hasSensorType(sensor_id)){
                            getSensorValue(count, SensorType.getSensorName(sensor_id), Float.parseFloat(value));
                            count++;
                        }

//                        sensor_values[Integer.parseInt(sensor_id)-1] = Float.parseFloat(value);
//                        System.out.println("sensor_id : " + (Integer.parseInt(sensor_id)) + " , value : " + sensor_values[Integer.parseInt(sensor_id)-1]);
                    }

                    time.setText(DateUtils.formatDateTime(getBaseContext(), timeStampFormat.parse(time_stamp).getTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ueep5tof21.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/sensors";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void getSensorValue(int sensorId, String searchDateStart, String searchDateEnd, String sensorName){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray sensorValueList = jsonResponse.getJSONArray("data");

                    reference_timestamp = 0;
                    String strTime;
                    Date timeStamp;

                    for(int i=0; i<sensorValueList.length(); i++) {
                        strTime = sensorValueList.getJSONObject(i).getString("time_stamp");
                        timeStamp = timeStampFormat.parse(strTime);
//                        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeStamp);
//                        timeStamp = dateTimeFormat.parse(dateTime);

                        if(i==0){
                            reference_timestamp = timeStamp.getTime();
                        }

                        sensor_entry.add(new Entry(
                                timeStamp.getTime()-reference_timestamp,
                                Float.parseFloat(sensorValueList.getJSONObject(i).getString("value"))
                                )
                        );
//                        System.out.println("timeStamp.getTime() : " + timeStamp.getTime());
//                        System.out.println("timeStamp : " + timeStamp);
//                        System.out.println("datetime : " + timeStamp.getTime());
//                        System.out.println("value : " + Float.parseFloat(sensorValueList.getJSONObject(i).getString("value")));
                    }
                    chart();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ueep5tof21.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/sensors/"+sensorId+"/"+searchDateStart+"/"+searchDateEnd+"?sensorName="+sensorName;
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void chart() {
        lineChart = (LineChart)findViewById(R.id.chart);
        lineChart.invalidate();
        lineChart.clear();
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        Collections.sort(sensor_entry, new EntryXComparator());
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
//        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss");

            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.MILLISECONDS.toMillis((long) value+reference_timestamp);
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
                verify.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            setOnOffSettings();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

                verify.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            setThresholdSettings();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                verify.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            setValueSettings();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                getOnOffSettings();
                break;
            case 1:
                frame.addView(thresholdView);
                getThresholdSettings();
                break;
            case 2:
                frame.addView(settingView);
                getValueSettings();
                break;
        }
    }

    private void getSensorValue(int i, String name, float value) {
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
        mButton.setText(name + "\n" + value); //버튼에 들어갈 텍스트를 지정(String)
        mButton.setTextSize(13);
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

    private void getOnOffSettings(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray onOffSettingList = jsonResponse.getJSONArray("data");

                    onoffData = new ArrayList<>();
                    onOffItem = new OnOffItemData();

                    for(int i=0; i<onOffSettingList.length(); i++) {
                        String id = onOffSettingList.getJSONObject(i).getString("id");

                        if(SettingType.hasOnOffSetting(id)){
                            onOffItem = new OnOffItemData();
                            onOffItem.id = onOffSettingList.getJSONObject(i).getString("id");
                            onOffItem.name = onOffSettingList.getJSONObject(i).getString("description");
                            onOffItem.value = onOffSettingList.getJSONObject(i).getString("VALUE");
                            onOffItem.time_stamp = onOffSettingList.getJSONObject(i).getString("time_stamp");
                            onoffData.add(onOffItem);
                        }
                    }

                    onoffView = (ListView)findViewById(R.id.onoff);
                    onOffAdapter = new OnOffListAdapter(onoffData);
                    onoffView.setAdapter(onOffAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ueep5tof21.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/settings";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void setOnOffSettings() throws UnsupportedEncodingException, JSONException {
        settingDatas = "";
        for(int i=0; i<onoffData.size(); i++){
            if(i==onoffData.size()-1)
                settingDatas = settingDatas+onOffAdapter.getItemValue(i);
            else
                settingDatas = settingDatas+onOffAdapter.getItemValue(i)+", ";
        }

        byte[] bytes = settingDatas.getBytes("UTF-8");
        String sendBody = new String(bytes, Charset.forName("UTF-8"));

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    String status = response.getString("status");

                    if(status.equals("1"))
                        Toast.makeText(getApplicationContext(), "On/Off 제어가 설정되었습니다.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "On/Off 제어에 실패하였습니다.", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("area_id", area_id);
        jsonRequest.put("datas", sendBody);

        System.out.println("jsonRequest : " + jsonRequest);

        String url = "http://113.198.235.230:18080/app/setting";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.POST, url, jsonRequest, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void getThresholdSettings(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray thresholdSettingList = jsonResponse.getJSONArray("data");

                    thresholdData = new ArrayList<>();
                    thresholdItem = new ThresholdItemData();

                    int count = 0;
                    for(int i=0; i<thresholdSettingList.length(); i++) {
                        String id = thresholdSettingList.getJSONObject(i).getString("id");

                        if(SettingType.hasThresholdSetting(id)){
                            if(count%2==0) {
                                thresholdItem = new ThresholdItemData();
                                thresholdItem.id = thresholdSettingList.getJSONObject(i).getString("id");
                                thresholdItem.name = thresholdSettingList.getJSONObject(i).getString("description");
                                thresholdItem.minValue = thresholdSettingList.getJSONObject(i).getString("VALUE");
                                thresholdItem.time_stamp = thresholdSettingList.getJSONObject(i).getString("time_stamp");
                            } else {
                                thresholdItem.maxValue = thresholdSettingList.getJSONObject(i).getString("VALUE");
                                thresholdData.add(thresholdItem);
                            }
                            count++;
                        }
                    }
                    thresholdView = (ListView)findViewById(R.id.threshold);
                    threshouldAdapter = new ThreshouldListAdapter(thresholdData);
                    thresholdView.setAdapter(threshouldAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ueep5tof21.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/settings";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void setThresholdSettings() throws UnsupportedEncodingException, JSONException {
        settingDatas = "";

        for(int i=0; i<thresholdData.size(); i++){
            if(i==thresholdData.size()-1){
                settingDatas = settingDatas+threshouldAdapter.getItemMinValue(i)+", ";
                settingDatas = settingDatas+threshouldAdapter.getItemMaxValue(i);
            }
            else{
                settingDatas = settingDatas+threshouldAdapter.getItemMinValue(i)+", ";
                settingDatas = settingDatas+threshouldAdapter.getItemMaxValue(i)+", ";
            }
        }

        byte[] bytes = settingDatas.getBytes("UTF-8");
        String sendBody = new String(bytes, Charset.forName("UTF-8"));

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    String status = response.getString("status");

                    if(status.equals("1"))
                        Toast.makeText(getApplicationContext(), "임계값이 설정되었습니다.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "임계값 설정에 실패하였습니다.", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("area_id", area_id);
        jsonRequest.put("datas", sendBody);

        System.out.println("jsonRequest : " + jsonRequest);

        String url = "http://113.198.235.230:18080/app/threshold";
        System.out.println("url : " + url);
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.POST, url, jsonRequest, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void getValueSettings(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray valueSettingList = jsonResponse.getJSONArray("data");

                    settingData = new ArrayList<>();
                    settingItem = new SettingItemData();

                    for(int i=0; i<valueSettingList.length(); i++) {
                        String id = valueSettingList.getJSONObject(i).getString("id");

                        if(SettingType.hasValueSetting(id)){
                            settingItem = new SettingItemData();
                            settingItem.id = valueSettingList.getJSONObject(i).getString("id");
                            settingItem.name = valueSettingList.getJSONObject(i).getString("description");
                            settingItem.value = valueSettingList.getJSONObject(i).getString("VALUE");
                            settingItem.time_stamp = valueSettingList.getJSONObject(i).getString("time_stamp");
                            settingData.add(settingItem);
                        }
                    }
                    settingView = (ListView)findViewById(R.id.setting);
                    settingAdapter = new SettingListAdapter(settingData);
                    settingView.setAdapter(settingAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ueep5tof21.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/settings";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void setValueSettings() throws UnsupportedEncodingException, JSONException {
        settingDatas = "";
        for(int i=0; i<settingData.size(); i++){
            if(i==settingData.size()-1)
                settingDatas = settingDatas+settingAdapter.getItemValue(i);
            else
                settingDatas = settingDatas+settingAdapter.getItemValue(i)+", ";
        }

        byte[] bytes = settingDatas.getBytes("UTF-8");
        String sendBody = new String(bytes, Charset.forName("UTF-8"));

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    String status = response.getString("status");

                    if(status.equals("1"))
                        Toast.makeText(getApplicationContext(), "목표값이 설정되었습니다.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "목표값 설정에 실패하였습니다.", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("area_id", area_id);
        jsonRequest.put("datas", sendBody);

        System.out.println("jsonRequest : " + jsonRequest);

        String url = "http://113.198.235.230:18080/app/target";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.POST, url, jsonRequest, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }

    private void getErrors(String searchDateStart, String searchDateEnd){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray errorList = jsonResponse.getJSONArray("data");

                    errorData = new ArrayList<>();
                    errorItem = new ErrorItemData();

                    for(int i=0; i<errorList.length(); i++) {
                        errorItem = new ErrorItemData();
                        errorItem.sensor_type = errorList.getJSONObject(i).getString("sensor_type");
                        errorItem.error_info = errorList.getJSONObject(i).getString("error_info");

                        if(!errorList.getJSONObject(i).getString("time_stamp").equals("null")) {
                            Date time_stamp = timeStampFormat.parse(errorList.getJSONObject(i).getString("time_stamp"));
                            errorItem.time_stamp = dateFormat.format(time_stamp) + "\n" + timeFormat.format(time_stamp);
                        }
                        if(!errorList.getJSONObject(i).getString("repair_time").equals("null")) {
                            Date repair_time = timeStampFormat.parse(errorList.getJSONObject(i).getString("repair_time"));
                            errorItem.repair_time = dateFormat.format(repair_time) + "\n" + timeFormat.format(repair_time);
                        }

                        errorData.add(errorItem);
                    }
                    errorView = (ListView)findViewById(R.id.error_table);
                    errorAdapter = new ErrorItemListAdapter(errorData);
                    errorView.setAdapter(errorAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ueep5tof21.execute-api.ap-northeast-2.amazonaws.com/dev/areas/"+area_id+"/errors"+"/"+searchDateStart+"/"+searchDateEnd;
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(DashBoardActivity.this);
        queue.add(commonGetHttpRequest);
    }
}
