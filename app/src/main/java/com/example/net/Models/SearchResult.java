package com.example.net.Models;

public class SearchResult {

    String profileImg, name, fields ;

    public SearchResult() {
    }

    public SearchResult(String profileImage, String userName, String userField) {
        this.profileImg = profileImage;
        this.name = userName;
        this.fields = userField;
    }

    public SearchResult(String name, String fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

}
