package com.example.smartfarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout farm_list;
    Farm farm1;
    Farm farm2;
    Farm farm3;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backImg = (ImageView) findViewById(R.id.main_back);
        backImg.setOnClickListener(new View.OnClickListener() {        // Image를 클릭한 경우
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        LoginActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

//        String farm_image[] = new String[3];
//        farm_image[0] = "farm1";
//        farm_image[1] = "farm2";
//        farm_image[2] = "farm3";

        farm_list = (LinearLayout) findViewById(R.id.farm_list);

        farm1 = new Farm(this);
        farm1.setFarmImage(R.drawable.farm1);
        farm1.setFarmName("코끼리하마농장");
        farm1.setFarmLocation("전라남도보성군");
        farm1.setFarmTotalArea("총6동");
        farm_list.addView(farm1);

        farm1.setOnClickListener(new View.OnClickListener() {        // Image를 클릭한 경우
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        AreaActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        farm2 = new Farm(this);
        farm2.setFarmImage(R.drawable.farm2);
        farm2.setFarmName("돼지사과농장");
        farm2.setFarmLocation("경상남도진주시");
        farm2.setFarmTotalArea("총3동");
        farm_list.addView(farm2);

        farm3 = new Farm(this);
        farm3.setFarmImage(R.drawable.farm3);
        farm3.setFarmName("커피농장");
        farm3.setFarmLocation("충청북도천안시");
        farm3.setFarmTotalArea("총2동");
        farm_list.addView(farm3);

    }
}
