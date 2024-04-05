package com.example.ass2coen390;

public class Access {
    private int accessId;
    private int profileId;
    private String accessType;
    private String timestamp;

    public Access(int accessId, int profileId, String accessType, String timestamp) {
        this.accessId = accessId;
        this.profileId = profileId;
        this.accessType = accessType;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}