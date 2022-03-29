package com.waveneuro.data.model.entity;

import com.asif.abase.data.model.BaseModel;

public class User extends BaseModel {

    private String email;
    private String username;
    private String name;
    private String givenName;
    private String familyName;
    private String birthdate;
    private String imageThumbnailUrl;
    private String gender;
    private String location;
    private String customGoal;

    public User() {
    }

    public User(String email, String username, String name, String givenName, String familyName,
                String birthdate, String imageThumbnailUrl, String gender, String location,
                String customGoal) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.birthdate = birthdate;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.gender = gender;
        this.location = location;
        this.customGoal = customGoal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomGoal() {
        return customGoal;
    }

    public void setCustomGoal(String customGoal) {
        this.customGoal = customGoal;
    }
}
