
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AreaActivity extends AppCompatActivity implements View.OnClickListener {
    String farm_id;
    String id, area_number, time_stamp;
    Button areaButton;
    ArrayList<Button> areaButtons;

    GridLayout gl;
    ScrollView sv;
    ImageView backImg;
    GridLayout.LayoutParams params;

    int width, pixels;
    float dp, fpixels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        Intent intent = getIntent();
        farm_id = intent.getStringExtra("farm_id");

        areaButtons = new ArrayList<>();

        backImg = (ImageView) findViewById(R.id.area_back);
        backImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        width = dm.widthPixels;
        // int height = dm.heightPixels;
        gl = (GridLayout) findViewById(R.id.area_grid);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        dp = 40f;
        fpixels = metrics.density * dp;
        pixels = (int) (fpixels + 0.5f);

        setAreaList();
    }

    private void setAreaList(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = response;
                    JSONArray farmList = jsonResponse.getJSONArray("data");

                    for(int i=0; i<farmList.length(); i++) {
                        id = farmList.getJSONObject(i).getString("id");
                        area_number = farmList.getJSONObject(i).getString("area_number");
                        time_stamp = farmList.getJSONObject(i).getString("time_stamp");

                        areaButton = new Button(AreaActivity.this);
                        areaButton.setWidth((width - pixels*3)/2);
                        areaButton.setHeight((width - pixels*3)/2);
                        areaButton.setText(area_number+"동");
                        areaButton.setId(Integer.parseInt(id));
                        areaButtons.add(areaButton);

                        params = new GridLayout.LayoutParams();
                        if(i%2==0){
                            params.setMargins(0,0,pixels/2,pixels);
                        } else{
                            params.setMargins(pixels/2,0,0,pixels);
                        }

                        final int position = i+1;
                        gl.addView(areaButton, i, params);

                    }

                    for(int i=0; i<farmList.length(); i++) {
                        areaButtons.get(i).setOnClickListener(AreaActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://uxilt2y0g6.execute-api.ap-northeast-2.amazonaws.com/dev/farms/"+farm_id+"/areas";
        CommonGetHttpRequest commonGetHttpRequest = new CommonGetHttpRequest(Request.Method.GET, url, null, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(AreaActivity.this);
        queue.add(commonGetHttpRequest);
    }

    @Override
    public void onClick(View v) {
        Button targetFarm = (Button) v;
        for(Button areaButton : areaButtons) {
            if(areaButton == targetFarm) {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                intent.putExtra("area_id", Integer.toString(areaButton.getId()));
                startActivity(intent);
            }
        }
    }
}
