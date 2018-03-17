package com.example.android.ereportpolice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.models.Announcement;
import com.example.android.ereportpolice.models.App;
import com.example.android.ereportpolice.utils.Utils;
import com.squareup.picasso.Picasso;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class DetailedAnnouncementActivity extends AppCompatActivity {
    Box<Announcement> announcements;
    TextView tv_title, tv_message;
    ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_announcement);

        //getting the id of the announcement from the intent extras
        Intent intent = getIntent();
        String extras = intent.getStringExtra("announcement_id");
        long announcement_id = Long.parseLong(extras);

        local_storage_setup();

        //fetching the particular announcement
        Announcement announcement = announcements.get(announcement_id);

        //initializing the UI elements
        tv_title = findViewById(R.id.tv_title);
        tv_message = findViewById(R.id.tv_content);
        iv_image = findViewById(R.id.iv_image);

        //setting the details to the screen
        tv_title.setText(announcement.getTitle());
        tv_message.setText(announcement.getMessage());
        String image_url = Utils.SERVER_URL + "final_proj_api/public/images/" + announcement.getImage();
        Picasso.with(getApplicationContext()).load(image_url).into(iv_image);
    }

    private void local_storage_setup() {
        //ObjectBox set up
        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        announcements = boxStore.boxFor(Announcement.class);
    }


}
