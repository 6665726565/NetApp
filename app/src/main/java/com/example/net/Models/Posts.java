package com.example.net.Models;

public class Posts {
    private String key , uid , userName ,userProfileImg , uField, fields, postImg , postDescription, likesNumber , commentsNumber;

    public Posts() {
    }

    public Posts(String key , String uid ,String userName, String userProfileImg ,String userField,String fields, String postImg, String postDescription , String likesNumber, String comntsNumber) {
        this.key = key;
        this.uid = uid;
        this.userName = userName;
        this.userProfileImg = userProfileImg;
        this.uField = userField;
        this.fields = fields;
        this.postImg = postImg;
        this.postDescription = postDescription;
        this.likesNumber = likesNumber;
        this.commentsNumber = comntsNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuField() {
        return uField;
    }

    public void setuField(String uField) {
        this.uField = uField;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(String likesNumber) {
        this.likesNumber = likesNumber;
    }

    public String getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(String commentsNumber) {
        this.commentsNumber = commentsNumber;
    }
}
