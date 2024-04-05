package com.example.ass2coen390;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "profileDatabase";
    private static final int DATABASE_VERSION = 1;

    // Profile table
    private static final String TABLE_PROFILE = "Profile";
    private static final String COLUMN_PROFILE_ID = "ProfileID";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_SURNAME = "Surname";
    private static final String COLUMN_GPA = "GPA";
    private static final String COLUMN_CREATION_DATE = "CreationDate";

    // Access table
    private static final String TABLE_ACCESS = "Access";
    private static final String COLUMN_ACCESS_ID = "AccessID";
    private static final String COLUMN_ACCESS_TYPE = "AccessType";
    private static final String COLUMN_TIMESTAMP = "Timestamp";

    // SQL statements to create tables
    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE "
            + TABLE_PROFILE + "("
            + COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_SURNAME + " TEXT,"
            + COLUMN_GPA + " REAL,"
            + COLUMN_CREATION_DATE + " TEXT" + ")";

    private static final String CREATE_TABLE_ACCESS = "CREATE TABLE "
            + TABLE_ACCESS + "("
            + COLUMN_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PROFILE_ID + " INTEGER,"
            + COLUMN_ACCESS_TYPE + " TEXT,"
            + COLUMN_TIMESTAMP + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_PROFILE_ID + ") REFERENCES " + TABLE_PROFILE + "(" + COLUMN_PROFILE_ID + "))";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //execute SQL -> create Profile and Access tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_ACCESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Add a profile to the database
    public Profile addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_ID, profile.getId()); // Assuming AUTOINCREMENT is removed
        values.put(COLUMN_NAME, profile.getName());
        values.put(COLUMN_SURNAME, profile.getSurname());
        values.put(COLUMN_GPA, profile.getGpa());
        values.put(COLUMN_CREATION_DATE, profile.getCreationDate());

        // Insert the profile record into the database
        long profileId = db.insert(TABLE_PROFILE, null, values);

        if (profileId != -1) {
            // Successfully inserted the profile
            profile.setId((int) profileId); // Update the profile's ID with the generated one

            // add an access record for the creation
            Access creationAccess = new Access(0, (int) profileId, "Creation", profile.getCreationDate());
            addAccess(creationAccess);
        }

        db.close();
        return profile;
    }

    // Add an access entry to the database
    public Access addAccess(Access access) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PROFILE_ID, access.getProfileId());
        values.put(COLUMN_ACCESS_TYPE, access.getAccessType());
        values.put(COLUMN_TIMESTAMP, access.getTimestamp());

        long id = db.insert(TABLE_ACCESS, null, values);
        Log.d("DatabaseHelper", "Inserted access record with ID: " + id);
        db.close();

        access.setAccessId((int) id); // Update the access's ID with the generated one
        return access;
    }

    // Read profiles from the database
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFILE, new String[] { COLUMN_PROFILE_ID, COLUMN_NAME, COLUMN_SURNAME, COLUMN_GPA, COLUMN_CREATION_DATE }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3), cursor.getString(4));
                profiles.add(profile);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return profiles;
    }

    // Read access entries for a specific profile
    public List<Access> getAccessByProfileId(int profileId) {
        List<Access> accesses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCESS, new String[] { COLUMN_ACCESS_ID, COLUMN_PROFILE_ID, COLUMN_ACCESS_TYPE, COLUMN_TIMESTAMP }, COLUMN_PROFILE_ID + "=?", new String[] { String.valueOf(profileId) }, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Access access = new Access(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3));
                accesses.add(access);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        Log.d("DatabaseHelper", "Number of access records retrieved: " + accesses.size());
        return accesses;
    }

    // Delete a profile and its related access entries from the database
    // (I created that function but didn't implemented it in the end because in the word doc, the app UI had no apparent delete button)
    public void deleteProfile(int profileId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First, delete all related access entries
        db.delete(TABLE_ACCESS, COLUMN_PROFILE_ID + "=?", new String[] {String.valueOf(profileId)});

        // Then, delete the profile
        db.delete(TABLE_PROFILE, COLUMN_PROFILE_ID + "=?", new String[] {String.valueOf(profileId)});

        db.close();
    }

}