package com.example.smartfarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {
    Button joinBtn;
    Button loginBtn;

    boolean checkb;

    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_btn);
        joinBtn = (Button) findViewById(R.id.login_join_btn);

        final EditText idText = (EditText) findViewById(R.id.login_id_input);
        final EditText pwText = (EditText) findViewById(R.id.login_pw_input);

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idText.getText().toString();
                final String password = pwText.getText().toString();
                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                int login_status = jsonResponse.getInt("login_status");

                                if (login_status == 1) {
                                    if(checkb == true){
                                        editor.putString("inputId", id);
                                        editor.putString("inputPw", password);
                                        editor.putBoolean("inputCheck", true);
                                        editor.commit();
                                    }
                                    int user_id = jsonResponse.getInt("user_id");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    startActivity(intent);
                                    finish();
                                } else if (login_status == 2) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("존재하는 이메일이 없습니다.")
                                            .setNegativeButton("AGAIN", null)
                                            .create()
                                            .show();
                                } else if (login_status == 3) {
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
