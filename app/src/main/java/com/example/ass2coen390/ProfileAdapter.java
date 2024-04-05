package com.example.ass2coen390;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<Profile> profileList;
    private Context context;
    private String displayMode = "SURNAME"; // Default to surname

    // Constructor
    public ProfileAdapter(List<Profile> profileList, Context context) {
        this.profileList = profileList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_list_item, parent, false);
        return new ViewHolder(view, context, profileList);
    }

    //bind data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        // Check the displayMode and set text accordingly
        switch (displayMode) {
            case "SURNAME":
                holder.profileName.setText((position + 1) + ".  "+ profile.getName() + ", " + profile.getSurname());
                break;
            case "ID":
                holder.profileName.setText((position + 1) + ".  " + String.valueOf(profile.getId()));
                break;
            default:
                holder.profileName.setText((position + 1) + ".  " +profile.getName() + ", " + profile.getSurname()); // Fallback to surname
        }
    }

    //return the size of the profileList
    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView profileName;

        // Constructor for ViewHolder
        public ViewHolder(View itemView, final Context context, final List<Profile> profileList) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profileNameDisplay);

            //handle clicks on profile items
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Profile profile = profileList.get(position);

                        // Log an "OPENED" access event in the database
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        Access access = new Access(0, profile.getId(), "OPENED", getCurrentTimestamp()); // Adjust parameters as needed
                        databaseHelper.addAccess(access);

                        //pass profile details via intent
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("ProfileID", profile.getId());
                        intent.putExtra("Name", profile.getName());
                        intent.putExtra("Surname", profile.getSurname());
                        intent.putExtra("GPA", profile.getGpa());
                        intent.putExtra("CreationDate", profile.getCreationDate());

                        context.startActivity(intent);

                        //debug stuff
                        Log.d("ProfileClick", "ID: " + profile.getId() + ", Name: " + profile.getName() + ", CreationDate: " + profile.getCreationDate());

                    }
                }
            });
        }
    }

    //change the display mode of profiles
    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    //get timestamp
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


}
