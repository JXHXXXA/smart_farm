
package com.example.smartfarm;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

public class AreaActivity extends AppCompatActivity {
    Button areaButton;
    GridLayout gl;
    ScrollView sv;
    ImageView backImg;
    GridLayout.LayoutParams params;

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

        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)20, getResources().getDisplayMetrics());
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        gl = (GridLayout) findViewById(R.id.area_grid);

        // areaButton.setId(Integer.parseInt("area_"+num));
        // areaButton.setOnClickListener(mOnClick); //버튼에 OnClickListener를 지정(OnClickListener)

        for (int i = 0; i < 8; i++) {
            areaButton = new Button(this);
            areaButton.setWidth(width/2-value*3);
            areaButton.setHeight(width/2-value*3);
            areaButton.setText(Integer.toString(i + 1) + "동");
            areaButton.setId(i);

            params = new GridLayout.LayoutParams();
            if(i%2==0){
                params.setMargins(0,0,value,value*2);
            } else{
                params.setMargins(value,0,0,value*2);
            }

            final int position = i+1;
            gl.addView(areaButton, i, params);

            areaButton.setOnClickListener(new View.OnClickListener() {
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
