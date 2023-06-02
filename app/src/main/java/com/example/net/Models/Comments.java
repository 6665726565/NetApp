package com.example.net.Models;

public class Comments {
    String id , comment , profileImg, userName;

    public Comments() {
    }

    public Comments(String id, String comment, String userImg, String userName) {
        this.id = id;
        this.comment = comment;
        this.profileImg = userImg;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
