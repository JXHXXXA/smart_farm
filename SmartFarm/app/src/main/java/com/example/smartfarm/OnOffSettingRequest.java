package com.example.smartfarm;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class OnOffSettingRequest extends JsonRequest<JSONObject> {

    final static private String URL = "http://113.198.235.230:18080/app/setting";
    private Map<String, String> parameters;

    public OnOffSettingRequest(int method, String url, JSONObject jsonRequest,
                                Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);

//        parameters = new HashMap<>();
//        parameters.put("area_id", area_id);
//        parameters.put("datas", datas);
//        System.out.println("area_id : "+area_id);
//        System.out.println("datas : "+datas);
    }

//    @Override
//    public String getBodyContentType() {
//        return "application/x-www-form-urlencoded; charset=UTF-8";
//    }

//    @Override
//    protected Response<String> parseNetworkResponse(NetworkResponse response) {
//        String parsed;
//        try {
//            parsed = new String(response.data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            parsed = new String(response.data);
//        }
//        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
//    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
