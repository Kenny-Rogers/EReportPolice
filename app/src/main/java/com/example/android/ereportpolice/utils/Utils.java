package com.example.android.ereportpolice.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krogers on 1/26/18.
 */

public class Utils {
    //holds the IP ADDRESS or URL of the server
    public static final String SERVER_URL = "http://192.168.8.100/";
    private static final String TAG = "Utils";

    public static void publish_location(final Context context, final String lat, final String lon) {
        //URL
        String url = Utils.SERVER_URL + "final_proj_api/public/register_user.php?user_type=location";

        Map<String, String> params = new HashMap<>();
        params.put("geo_lat", lat);
        params.put("geo_long", lon);
        params.put("user_id", "4");
        params.put("type_of_user", "patrol_team");

        //sending details to server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("server_response", "onResponse: " + response);
                try {
                    //  JSONObject object = new JSONObject(response);
                    if (response.getInt("status") == 1) {
                        Log.i(TAG, "location details {lat: " + lat + " and lng: " + lon + "} sent successfully");
                    } else {
                        Log.i(TAG, "Failed to send data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to send data" + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        NetworkUtil.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }


}
