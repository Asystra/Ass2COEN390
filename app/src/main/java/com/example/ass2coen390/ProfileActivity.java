package com.example.ass2coen390;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class ProfileActivity extends AppCompatActivity {

    // Declaration of TextView
    protected TextView namee;
    protected TextView surnamee;
    protected TextView idd;
    protected TextView gpaa;
    protected TextView creation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup toolbar with back navigation enabled
        Toolbar myToolbar = (Toolbar) findViewById(R.id.profileActivity_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Retrieve data
        Intent intent = getIntent();
        int profileId = intent.getIntExtra("ProfileID", -1);
        String name = intent.getStringExtra("Name");
        String surname = intent.getStringExtra("Surname");
        float gpa = intent.getFloatExtra("GPA", 0); // Default to 0 if not found
        String creationDate = intent.getStringExtra("CreationDate");

        // Find TextViews by ID and set the text
        namee =  findViewById(R.id.profileNameActivity);
        namee.setText("Name: " + name);
        surnamee =  findViewById(R.id.profileSurnameActivity);
        surnamee.setText("Surname: " + surname);
        idd = findViewById(R.id.profileIDActivity);
        idd.setText("ID: " + profileId);
        gpaa = findViewById(R.id.profileGPAActivity);
        gpaa.setText(String.format("GPA: %.2f", gpa));
        creation = findViewById(R.id.profileCreatedActivity);
        creation.setText("Profile Created: " + creationDate);

        //Database to fetch access records
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Access> accessList = databaseHelper.getAccessByProfileId(profileId);
        List<String> accessStrings = new ArrayList<>();

        //Convert access records to string format for display
        for (Access access : accessList) {
            // Assuming your timestamp is already in the desired format
            String accessRecord = access.getAccessType() + ": " + access.getTimestamp();
            accessStrings.add(accessRecord);
        }

        // Setup ListView to display access records
        ListView listView = findViewById(R.id.listViewProfileActvity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accessStrings);
        listView.setAdapter(adapter);
        Log.d("AccessListSize", "Size: " + accessList.size());
        for (String access : accessStrings) {
            Log.d("AccessRecord", access);
        }
    }

    // add Access closed when returning to the mainActivity
    @Override
    protected void onPause() {
        super.onPause();

        // Log the 'closed' event for the profile
        Intent intent = getIntent();
        int profileId = intent.getIntExtra("ProfileID", -1);
        if (profileId != -1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            String currentTimestamp = getCurrentTimestamp();
            Access access = new Access(0, profileId, "CLOSED", currentTimestamp);
            databaseHelper.addAccess(access);

        }
    }

    //get the time when activity closed
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
