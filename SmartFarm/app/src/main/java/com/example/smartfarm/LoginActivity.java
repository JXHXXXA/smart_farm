package com.example.smartfarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button joinBtn;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_btn);
        joinBtn = (Button) findViewById(R.id.login_join_btn);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View veiw) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        JoinActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

    }
}
