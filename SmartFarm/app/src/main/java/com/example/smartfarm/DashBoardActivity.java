package com.example.smartfarm;

import android.content.Entity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.AlphabeticIndex;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity {
    Button mButton;
    GridLayout gl;
    GridLayout.LayoutParams params;
    TextView time;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("현재값");
        tabHost1.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("장치설정");
        tabHost1.addTab(ts2);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("통계");
        tabHost1.addTab(ts3);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 4"), (페이지 뷰:"content4")
        TabHost.TabSpec ts4 = tabHost1.newTabSpec("Tab Spec 4");
        ts4.setContent(R.id.content4);
        ts4.setIndicator("오류");
        tabHost1.addTab(ts4);

        /* ---------------------------Grid-------------------------- */
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        gl = (GridLayout) findViewById(R.id.dash_board_grid);

        System.out.println("dm : " + dm.density);
        System.out.println("width : " + width);
        // mButton.setId(Integer.parseInt("area_"+num));
        // mButton.setOnClickListener(mOnClick); //버튼에 OnClickListener를 지정(OnClickListener)

        for (int i = 0; i < 16; i++) {
            mButton = new Button(this);
//            mButton.setId(@+id/area_bt);
            mButton.setWidth(width / 5);
            mButton.setHeight(width / 4);
            mButton.setText("센서 " + Integer.toString(i + 1)); //버튼에 들어갈 텍스트를 지정(String)
            mButton.getBackground().setColorFilter(Color.parseColor("#b5ddc0"), PorterDuff.Mode.DARKEN);
            params = new GridLayout.LayoutParams();
            float num = dm.density;
            /* 첫번째 라인*/
            if (i % 4 == 0) {
                params.setMargins(Math.round(3 * num), Math.round(num), Math.round(num), Math.round(num));
                if (i >= 4)
                    params.setMargins(3 * Math.round(num), 0, Math.round(num), Math.round(num));
            } else { /*네번째 라인 */
                params.setMargins(0, Math.round(num), Math.round(num), Math.round(num));
                if (i >= 4)
                    params.setMargins(0, 0, Math.round(num), Math.round(num));
            }

            mButton.setId(i);

            final int position = i + 1;
            gl.addView(mButton, i, params);
        }



        /* 일단 현재 시간 받아오는거로..*/
        time = (TextView) findViewById(R.id.dash_board_time);
        time.setText(DateUtils.formatDateTime(getBaseContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE));



        /* chart */
        lineChart = (LineChart)findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));

        LineDataSet lineDataSet = new LineDataSet(entries, "왜안돼 씨부레");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
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
        xAxis.setGranularityEnabled(true);//

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setInverted(true);


        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");
        xAxis.setGranularityEnabled(true);
        yLAxis.setGranularityEnabled(true);

        lineChart.getXAxis().setYOffset(15.0f);
        lineChart.getAxisLeft().setXOffset(15.0f);



        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(3000, Easing.EaseInCubic);

        lineChart.invalidate();
 /*       lineChart = (LineChart)findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));

        LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
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

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
//        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.animateY(2000, Easing.EaseInCubic);
        lineChart.invalidate();
*/

    }

}
