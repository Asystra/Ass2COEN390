package com.example.ass2coen390;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Declare UI components, database and data manipulation
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;
    private DatabaseHelper databaseHelper;
    private TextView profileCountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the XML layout for this activity
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar myToolbar = findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(myToolbar);

        // Initialize UI components and RecyclerView with a LinearLayoutManager I saw that we needed to use a ListView after I implemented a recyclerview sorry...
        profileCountTextView = findViewById(R.id.profileCount);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database helper and load profiles from the database
        databaseHelper = new DatabaseHelper(this);
        profileList = databaseHelper.getAllProfiles(); // Fetch profiles from DB
        profileAdapter = new ProfileAdapter(profileList, this);
        recyclerView.setAdapter(profileAdapter);
        // Initially display profiles sorted by name
        displayProfilesByName();

        // Set up the button to add a new profile using a fragment
        Button fragButton = findViewById(R.id.fragmentButton);
        fragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileFrag addProfileFragment = new addProfileFrag();
                addProfileFragment.show(getSupportFragmentManager(), "AddProfileDialogFragment");
            }
        });
    }

    // Inflate the options menu from XML
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    // Handle selection of menu items to sort and display profiles differently
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Check which menu item was selected and update UI accordingly
        if (id == R.id.profileNameDisplay) {
            displayProfilesByName();
            profileAdapter.setDisplayMode("NAME");
        } else if (id == R.id.profileNameDisplay) { // Duplicate condition, likely a mistake
            displayProfilesByName();
            profileAdapter.setDisplayMode("SURNAME");
        } else if (id == R.id.profileIDDisplay) {
            displayProfilesByID();
            profileAdapter.setDisplayMode("ID");
        }
        // Refresh the RecyclerView to reflect changes
        profileAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    }

    // Helper method to update UI with the number of profiles and current sort mode
    private void updateProfileCountAndDisplayMode(String mode) {
        int profileCount = profileList.size();
        String displayModeText = profileCount + " Profiles, " + mode;
        profileCountTextView.setText(displayModeText);
    }

    // Sort profiles by surname and update RecyclerView
    private void displayProfilesByName() {
        Collections.sort(profileList, (p1, p2) -> p1.getSurname().compareToIgnoreCase(p2.getSurname()));
        profileAdapter.notifyDataSetChanged();
        updateProfileCountAndDisplayMode("by Surname");
    }

    // Sort profiles by ID and update RecyclerView
    private void displayProfilesByID() {
        Collections.sort(profileList, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
        profileAdapter.notifyDataSetChanged();
        updateProfileCountAndDisplayMode("by ID");
    }

    // Save a new profile to the database and update UI if the profile ID is unique
    public void saveProfileToDatabase(Profile profile) {
        boolean isUnique = true;
        for (Profile p : profileList) {
            if (p.getId() == profile.getId()) {
                Toast.makeText(this, "Duplicate ID. Profile not saved.", Toast.LENGTH_SHORT).show();
                isUnique = false;
                break;
            }
        }

        if (isUnique) {
            Profile newProfile = databaseHelper.addProfile(profile);

            // Add new profile to the list and notify the adapter
            int insertIndex = profileList.size();
            profileList.add(insertIndex, newProfile);
            profileAdapter.notifyItemInserted(insertIndex);

            // Optionally, scroll to the new item in the list
            recyclerView.scrollToPosition(insertIndex);
        }
    }
}


