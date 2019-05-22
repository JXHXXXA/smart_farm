package com.example.smartfarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button joinBtn, loginBtn;

    boolean checkb;
    EditText idText, pwText;

    SharedPreferences.Editor editor;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_btn);
        joinBtn = (Button) findViewById(R.id.login_join_btn);

        idText = (EditText) findViewById(R.id.login_id_input);
        pwText = (EditText) findViewById(R.id.login_pw_input);

//        loginBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View veiw) {
//                Intent intent = new Intent(
//                        getApplicationContext(),
//                        MainActivity.class);
//                startActivity(intent);
//            }
//        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        JoinActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idText.getText().toString();
                final String password = pwText.getText().toString();
                System.out.println("login onclick : " + id + ", " + password);

                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("else");
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("response : " + response);
                                JSONObject jsonResponse = new JSONObject(response);

                                String login_status = jsonResponse.getString("data");

                                if (login_status.equals("2")) {
                                    if (checkb == true) {
                                        editor.putString("inputId", id);
                                        editor.putString("inputPw", password);
                                        editor.putBoolean("inputCheck", true);
                                        editor.commit();
                                    }
//                                    int user_id = jsonResponse.getInt("user_id");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    intent.putExtra("user_id", user_id);
                                    startActivity(intent);
                                    finish();
                                } else if (login_status.equals("0")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("존재하는 이메일이 없습니다.")
                                            .setNegativeButton("AGAIN", null)
                                            .create()
                                            .show();
                                } else if (login_status.equals("1")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("비밀번호가 일치하지 않습니다.")
                                            .setPositiveButton("AGAIN", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(id, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            }
        });
    }
}
