package com.example.android.ereportpolice.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereportpolice.utils.LocationTracker;
import com.example.android.ereportpolice.utils.NetworkUtil;
import com.example.android.ereportpolice.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krogers on 2/1/18.
 * This class is activited every 5 secs after login to send the location to the server
 */

public class LocationAlarm extends BroadcastReceiver {
    String lat, lon;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //URL
        String url = Utils.SERVER_URL + "final_proj_api/public/update_info.php?user_type=location";
        LocationTracker gpStracker = new LocationTracker(context.getApplicationContext());
        final Location location = gpStracker.getLocation();


        if (location != null) {
            lat = location.getLatitude() + "";
            lon = location.getLongitude() + "";
            Toast.makeText(context.getApplicationContext(), "Lat: " + lat + "\n Lon:" + lon, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "Failed to get locations", Toast.LENGTH_LONG).show();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("status") == 4) {
                        Toast.makeText(context.getApplicationContext(), "Data sent successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Failed to send data", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error", "onErrorResponse: " + error.toString());
                Toast.makeText(context.getApplicationContext(), "Data sent failed", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("geo_lat", lat);
                params.put("geo_long", lon);
                params.put("team_id", "4");
                return params;
            }
        };

        NetworkUtil.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }
}
