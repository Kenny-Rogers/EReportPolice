package com.example.android.ereportpolice.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.activities.ComplaintDetailsActivity;
import com.example.android.ereportpolice.adapters.ComplaintAdapter;
import com.example.android.ereportpolice.models.Complaint;
import com.example.android.ereportpolice.utils.NetworkUtil;
import com.example.android.ereportpolice.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by krogers on 2/1/18.
 */

public class ComplainsFragment extends Fragment {
    private static final String TAG = "ComplainsFragment";
    protected String server_response;
    ListView root_view;
    ArrayList<Long> complaint_object_indexes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complain, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //equivalent to onCreate of an Activity
        root_view = view.findViewById(R.id.rootView1);

        final ArrayList<Complaint> complaintArrayList = new ArrayList<>();
        complaint_object_indexes = new ArrayList<>();

        String url = Utils.SERVER_URL + "final_proj_api/public/get_users_list.php?user_type=patrol_complainant&patrol_team_id=2";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i(TAG, "onResponse: response:" + response);
                    server_response = response;
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getString("status").equals("0")) {
                        JSONArray complaint_array = response_object.getJSONArray("complaint_objects");
                        Log.i(TAG, "onResponse: array: " + complaint_array);
                        for (int i = 0; i < complaint_array.length(); i++) {
                            JSONObject object = complaint_array.getJSONObject(i);
                            JSONObject complaint = object.getJSONObject("complaint");
                            complaint_object_indexes.add(Long.valueOf(complaint.getString("id")));
                            Log.i(TAG, "onResponse: complaint_object: " + complaint);
                            complaintArrayList.add(new Complaint(Integer.parseInt(complaint.getString("id")),
                                    complaint.getString("nature_of_issue"),
                                    complaint.getString("complainant_id"),
                                    complaint.getString("type_issue"),
                                    complaint.getString("date_time_of_report")));
                        }

                        final ComplaintAdapter complaintAdapter = new ComplaintAdapter(getContext(), complaintArrayList);

                        root_view.setAdapter(complaintAdapter);

                        root_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int item_id = complaint_object_indexes.indexOf(complaintAdapter.getItem(i).getId());
                                Intent detailed_view_intent = new Intent(getContext(), ComplaintDetailsActivity.class);
                                detailed_view_intent.putExtra("complaint_id", item_id);
                                detailed_view_intent.putExtra("server_response", server_response);
                                startActivity(detailed_view_intent);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "No new complaints made", Toast.LENGTH_SHORT).show();
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
        });

        NetworkUtil.getInstance(getContext()).addToRequestQueue(request);


    }
}
