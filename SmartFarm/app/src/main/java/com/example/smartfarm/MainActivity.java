package com.example.smartfarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView farm1Img;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        farm1Img = (ImageView) findViewById(R.id.farm1);
        farm1Img.setOnClickListener(new View.OnClickListener() {        // Image를 클릭한 경우
            public void onClick(View v) {
                Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    AreaActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        backImg = (ImageView) findViewById(R.id.main_back);
        backImg.setOnClickListener(new View.OnClickListener() {        // Image를 클릭한 경우
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        LoginActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }
}
