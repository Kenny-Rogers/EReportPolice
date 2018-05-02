package com.example.android.ereportpolice.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.utils.NetworkUtil;
import com.example.android.ereportpolice.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ComplaintDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ComplaintDetailsActivit";
    public ImageView iv_media;
    public VideoView vv_media;
    String server_response, location_object, complaint_media;
    int complaint_id;
    TextView tv_name, tv_telephone, tv_type_issue, tv_nature_of_issue, tv_date_time_of_report;
    private String type_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);
        Intent intent = getIntent();
        server_response = intent.getStringExtra("server_response");
        complaint_id = intent.getIntExtra("complaint_id", 0);
        init();
        displayDetails();
    }

    private void init() {
        tv_name = findViewById(R.id.tv_full_name);
        tv_date_time_of_report = findViewById(R.id.tv_date_time_of_report);
        tv_telephone = findViewById(R.id.tv_telephone);
        tv_nature_of_issue = findViewById(R.id.tv_nature_of_issue);
        tv_type_issue = findViewById(R.id.tv_type_issue);
        iv_media = findViewById(R.id.iv_media_image);
        vv_media = findViewById(R.id.vv_media_image);
        iv_media.setVisibility(View.GONE);
        vv_media.setVisibility(View.GONE);
    }

    private void displayDetails() {
        try {
            JSONObject object = new JSONObject(server_response);
            JSONArray complaint_array = object.getJSONArray("complaint_objects");
            JSONObject complaintJSONObject = complaint_array.getJSONObject(complaint_id);

            location_object = complaintJSONObject.getJSONObject("location").toString();
            if (complaintJSONObject.has("complaint_media")) {
                JSONObject mediaJSONObject = complaintJSONObject.getJSONObject("complaint_media");
                complaint_media = mediaJSONObject.getString("media_name");
                type_name = mediaJSONObject.getString("media_type");

                if ((type_name.equals("image/jpeg")) ||
                        (type_name.equals("image/jpg")) || (type_name.equals("image/pjpeg")) ||
                        (type_name.equals("image/x-png")) || (type_name.equals("image/png"))) {
                    iv_media.setVisibility(View.VISIBLE);
                    String image_url = Utils.SERVER_URL + "final_proj_api/public/complaint_media/" + complaint_media;
                    Picasso.with(getApplicationContext()).load(image_url).into(iv_media);
                } else {
                    vv_media.setVisibility(View.VISIBLE);
                    MediaController mediacontroller = new MediaController(this);
                    vv_media.setMediaController(mediacontroller);
                    mediacontroller.setAnchorView(vv_media);
                    String video_url = Utils.SERVER_URL + "final_proj_api/public/complaint_media/" + complaint_media;
                    Uri uri = Uri.parse(video_url);
                    vv_media.setVideoURI(uri);
                    vv_media.start();
                }

            }
            JSONObject complainantJSONObject = complaintJSONObject.getJSONObject("complainant");
            JSONObject complaint = complaintJSONObject.getJSONObject("complaint");

            String name = complainantJSONObject.getString("first_name") + " " +
                    complainantJSONObject.getString("other_names") + " " +
                    complainantJSONObject.getString("last_name") + " ";
            tv_name.setText(name);
            tv_telephone.setText(complainantJSONObject.getString("telephone"));

            tv_type_issue.setText(complaint.getString("type_issue"));
            tv_nature_of_issue.setText(complaint.getString("nature_of_issue"));
            tv_date_time_of_report.setText(complaint.getString("date_time_of_report"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void goto_location(View view) {
        //goto location activity
        Intent intent = new Intent(this, ComplaintMapView.class);
        intent.putExtra("location_details", location_object);
        startActivity(intent);
    }

    public void mark_attended(View view) {
        String url = Utils.SERVER_URL + "final_proj_api/public/update_info.php?user_type=complain_action";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSONObject = new JSONObject(response);
                    if (responseJSONObject.getString("status").equals("0")) {
                        Log.i(TAG, "onResponse: updating complain_action :" + responseJSONObject.getString("message"));
                    } else {
                        Log.i(TAG, "onResponse: updating complain_action :" + responseJSONObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", complaint_id + "");
                params.put("details_of_action", "attended to");
                return params;
            }
        };

        NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
