package com.example.android.ereportpolice.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.utils.LocationTracker;
import com.example.android.ereportpolice.utils.NetworkUtil;
import com.example.android.ereportpolice.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    private Button btn_sign_in;
    private EditText et_team_name, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init(); //initializing the variables
    }

    private void init() {
        btn_sign_in = findViewById(R.id.btn_sign_in);
        et_team_name = findViewById(R.id.et_team_name);
        et_password = findViewById(R.id.et_password);
    }

    public void sign_in(View view) {
        String sign_in_url = Utils.SERVER_URL + "final_proj_api/public/log_user_in.php";
        final String team_name = et_team_name.getText().toString();
        final String password = et_password.getText().toString();
        final String user_type = "patrol_team";

        StringRequest request = new StringRequest(Request.Method.POST, sign_in_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 4) {
                        //logging the user's location in
                        if (log_location(new LocationTracker(getApplicationContext()))) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to get Location Details", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Login Details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                Log.e("VolleyError", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", user_type);
                params.put("team_name", team_name);
                params.put("password", password);
                return params;
            }
        };

        //adding the request to the networkutil
        NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //registers the users location on the server for the first time
    private boolean log_location(LocationTracker locationTracker) {
        String url = Utils.SERVER_URL + "final_proj_api/public/register_user.php?user_type=location";
        String lat = "";
        String lon = "";
        Map<String, String> params = new HashMap<>();
        Location location = locationTracker.getLocation();

        if (location == null) {
            //locationTracker = new LocationTracker(getApplicationContext());
            //log_location(locationTracker);
            lat = "";
            lon = "";
            Toast.makeText(getApplicationContext(), "Failed to get locations", Toast.LENGTH_LONG).show();
        } else {
            lat = location.getLatitude() + "";
            lon = location.getLongitude() + "";
        }
            params.put("geo_lat", lat);
            params.put("geo_long", lon);
            params.put("team_id", "4");

            Toast.makeText(getApplicationContext(), "Lat: " + lat + "\n Lon:" + lon, Toast.LENGTH_SHORT).show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Data sent successfully", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley error", "onErrorResponse: " + error.toString());
                    Toast.makeText(getApplicationContext(), "Data sent failed", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);

            return true;
    }
}
