package com.example.android.ereportpolice.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.fragments.AnnouncementListFragment;
import com.example.android.ereportpolice.fragments.ComplainsFragment;
import com.example.android.ereportpolice.models.Announcement;
import com.example.android.ereportpolice.models.App;
import com.example.android.ereportpolice.utils.NetworkUtil;
import com.example.android.ereportpolice.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Box<Announcement> announcements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //setting up local storage
        local_storage_setup();

        //getting latest announcements
        sync_data();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            fragment = new ComplainsFragment();
        } else if (id == R.id.nav_gallery) {
            fragment = new AnnouncementListFragment();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.screen_area, fragment);

            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void local_storage_setup() {
        //ObjectBox set up
        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        announcements = boxStore.boxFor(Announcement.class);
    }

    //gets latest announcements
    private void sync_data() {
        final String date;
        String url;
        if (announcements.getAll().isEmpty()) {
            date = "";
        } else {
            ArrayList<Announcement> list_announcements = (ArrayList<Announcement>) announcements.getAll();
            int count = list_announcements.size();
            Announcement announcement = list_announcements.get(count - 1);
            date = announcement.getDate();
        }

        url = Utils.SERVER_URL + "final_proj_api/public/get_users_list.php?user_type=announcement&date=" + date;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responsearray = new JSONArray(response);
                    for (int i = 0; i < responsearray.length(); i++) {
                        JSONObject object = responsearray.getJSONObject(i);
                        Announcement announcement = new Announcement();
                        announcement.setImage(object.getString("image"));
                        announcement.setDate(object.getString("date_published"));
                        announcement.setMessage(object.getString("message"));
                        announcement.setTitle(object.getString("title"));
                        announcements.put(announcement);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Data returned", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No new announcements found", Toast.LENGTH_SHORT).show();
            }
        });

        NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
