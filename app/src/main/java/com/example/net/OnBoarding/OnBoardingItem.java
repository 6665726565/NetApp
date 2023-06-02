package com.example.net.OnBoarding;

public class OnBoardingItem {
    private String Title , description ;
    private int image;

    public OnBoardingItem(String title, String description, int img) {
        Title = title;
        this.description = description;
        this.image = img;
    }

    public OnBoardingItem() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
