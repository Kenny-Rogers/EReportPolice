package com.example.android.ereportpolice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private static final String TAG = "LoginScreen";

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
                        Log.i(TAG, jsonObject.getString("message"));
                        //logging the user's location in
                        if (log_location()) {
                            Log.i(TAG, "Location details received successfully");
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        } else {
                            Log.e(TAG, "Failed to get Location Details");
                        }
                    } else {
                        Log.e(TAG, "Invalid Login Details");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
    private boolean log_location() {
        //start location tracking service
        startService(new Intent(this, LocationTracker.class));

        return true;
    }
}
