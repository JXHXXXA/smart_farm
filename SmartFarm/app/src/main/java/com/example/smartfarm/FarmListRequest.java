package com.example.smartfarm;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FarmListRequest extends StringRequest {

    final static private String URL = "https://uxilt2y0g6.execute-api.ap-northeast-2.amazonaws.com/dev/farms";
    private Map<String, String> parameters;

    public FarmListRequest(String id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id",id);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
