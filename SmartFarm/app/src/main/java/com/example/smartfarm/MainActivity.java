package com.example.smartfarm;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String user_id;
    String area_num, id, name, owner, location, telephone, time_stamp;
    int farm_image[];
    LinearLayout farm_list;
    ArrayList<Farm> farms;
    Farm farm;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        backImg = (ImageView) findViewById(R.id.main_back);
        backImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        farm_list = (LinearLayout) findViewById(R.id.farm_list);

        farm_image = new int[3];
        farm_image[0] = R.drawable.farm1;
        farm_image[1] = R.drawable.farm2;
        farm_image[2] = R.drawable.farm3;

        farms = new ArrayList<>();
        setFarmList();
        System.out.println("setFarmList end");

    }
    private void setFarmList(){
        System.out.println("setFarmList start");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("response : " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray farmList = jsonResponse.getJSONArray("data");

                    for(int i=0; i<farmList.length(); i++) {
                        area_num = farmList.getJSONObject(i).getString("area_num");
                        id = farmList.getJSONObject(i).getString("id");
                        name = farmList.getJSONObject(i).getString("name");
                        owner = farmList.getJSONObject(i).getString("owner");
                        location = farmList.getJSONObject(i).getString("location");
                        telephone = farmList.getJSONObject(i).getString("telephone");
                        time_stamp = farmList.getJSONObject(i).getString("time_stamp");

                        farm = new Farm(MainActivity.this);
                        farm.setId(Integer.parseInt(id));
                        farm.setFarmImage(farm_image[(int)(Math.random() * 3)]);
                        farm.setFarmName(name);
                        farm.setFarmLocation(location);
                        farm.setFarmTotalArea("총 "+area_num+"동");
                        farm.setFarm_layout();
                        farms.add(farm);
                        farm_list.addView(farm);
                        System.out.println("farm.getId() : "+farm.getId());
                    }
                    for(int i=0; i<farmList.length(); i++) {
                        farms.get(i).setOnClickListener(MainActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        FarmListRequest listRequest = new FarmListRequest(String.valueOf(user_id), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(listRequest);
    }

    @Override
    public void onClick(View v) {
        Farm targetFarm = (Farm) v;
        for(Farm farm : farms)
        {
            if(farm == targetFarm)
            {
                System.out.println("onClick farm.getId() : "+farm.getId());
                Intent intent = new Intent(getApplicationContext(), AreaActivity.class);
                intent.putExtra("farm_id", Integer.toString(farm.getId()));
                startActivity(intent);
            }
        }
    }
}
