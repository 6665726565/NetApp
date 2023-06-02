package com.example.net.Models;

public class Users {
    private String uid, name , email , password , profileImg , fields ;
    private boolean isProfess;

    public Users() {
    }

    public Users( String uId ,  String name, String email, String password, String profileImg, String fields, boolean isProfess) {
        this.uid = uId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.fields = fields;
        this.isProfess = isProfess;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public boolean isProfess() {
        return isProfess;
    }

    public void setProfess(boolean profess) {
        isProfess = profess;
    }
}
