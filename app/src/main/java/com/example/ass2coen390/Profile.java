package com.example.ass2coen390;

public class Profile {
    private int profileID;
    private String name;
    private String surname;
    private float gpa;
    private String creationDate;

    public Profile(int profileID, String name, String surname, float gpa, String creationDate) {
        this.profileID = profileID;
        this.name = name;
        this.surname = surname;
        this.gpa = gpa;
        this.creationDate = creationDate;
    }

    // Getters and setters
    public int getId() {
        return profileID;
    }

    public void setId(int profileID) {
        this.profileID = profileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
