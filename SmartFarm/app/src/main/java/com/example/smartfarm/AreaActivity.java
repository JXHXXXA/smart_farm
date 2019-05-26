
package com.example.smartfarm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AreaActivity extends AppCompatActivity {
    AreaButton mButton;
    GridLayout gl;
    ScrollView sv;
    ImageView backImg;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        backImg = (ImageView) findViewById(R.id.area_back);
        backImg.setOnClickListener(new View.OnClickListener() {        // Image를 클릭한 경우
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        gl = (GridLayout) findViewById(R.id.area_grid);

        // mButton.setId(Integer.parseInt("area_"+num));
        // mButton.setOnClickListener(mOnClick); //버튼에 OnClickListener를 지정(OnClickListener)

        for (int i = 0; i < 8; i++) {
            mButton = new AreaButton(this);
            mButton.setBtnText(Integer.toString(i + 1) + "동");
            mButton.setId(i);

            final int position = i+1;
            gl.addView(mButton, i);

            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(
                            getApplicationContext(), // 현재 화면의 제어권자
                            DashBoardActivity.class); // 다음 넘어갈 클래스 지정
                    startActivity(intent); // 다음 화면으로 넘어간다
                    System.out.println(position);
                }
            });
        }
    }
}
