package com.example.android.ereportpolice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.android.ereportpolice.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class ComplaintMapView extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "ComplaintMapView";
    String location_details;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        location_details = intent.getStringExtra("location_details");
        Log.i(TAG, "onCreate: location details " + location_details);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng complaint_loc = new LatLng(0, 0);

        try {
            JSONObject locationJSONobject = new JSONObject(location_details);
            complaint_loc = new LatLng(Double.parseDouble(locationJSONobject.getString("geo_lat")),
                    Double.parseDouble(locationJSONobject.getString("geo_lat")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Add a marker in complaint_loc and move the camera

        mMap.addMarker(new MarkerOptions().position(complaint_loc).title("Marker to complaint"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(complaint_loc));
    }
}
