package com.example.android.ereportpolice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.ereportpolice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComplaintDetailsActivity extends AppCompatActivity {
    String server_response, location_object;
    int complaint_id;
    TextView tv_name, tv_telephone, tv_type_issue, tv_nature_of_issue, tv_date_time_of_report;

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
    }

    private void displayDetails() {
        try {
            JSONObject object = new JSONObject(server_response);
            JSONArray complaint_array = object.getJSONArray("complaint_objects");
            JSONObject complaintJSONObject = complaint_array.getJSONObject(complaint_id);

            location_object = complaintJSONObject.getJSONObject("location").toString();

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
}
