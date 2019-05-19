
package com.example.smartfarm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    Button mButton;
    GridLayout gl;
    GridLayout.LayoutParams params;
    ScrollView sv;
    ImageView backImg;


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

        System.out.println("dm : " + dm.density);
        System.out.println("width : " + width);
        // mButton.setId(Integer.parseInt("area_"+num));
        // mButton.setOnClickListener(mOnClick); //버튼에 OnClickListener를 지정(OnClickListener)

        for (int i = 0; i < 8; i++) {
            mButton = new Button(this);
//            mButton.setId(@+id/area_bt);
            mButton.setWidth(width / 3);
            mButton.setHeight(width / 3);
            mButton.setText(Integer.toString(i + 1) + "동"); //버튼에 들어갈 텍스트를 지정(String)
            mButton.getBackground().setColorFilter(Color.parseColor("#b5ddc0"), PorterDuff.Mode.DARKEN);
            params = new GridLayout.LayoutParams();

            /* 첫번째 라인*/
            if (i % 2 == 0) {
                params.setMargins(Math.round(45 * dm.density), Math.round(30 * dm.density), Math.round(30 * dm.density), Math.round(30 * dm.density));
                if (i >= 2)
                    params.setMargins(Math.round(45 * dm.density), 0, Math.round(30 * dm.density), Math.round(30 * dm.density));
            } else { /*두번째 라인 */
                params.setMargins(0, Math.round(30 * dm.density), Math.round(30 * dm.density), Math.round(30 * dm.density));
                if (i >= 2)
                    params.setMargins(0, 0, Math.round(30 * dm.density), Math.round(30 * dm.density));
            }

            mButton.setId(i);

            final int position = i+1;
            gl.addView(mButton, i, params);

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
