package com.example.net.Models;

public class Articles {
    private String id , title , description , image , field , readers , saveIc;

    public Articles() {
    }

    public Articles(String id, String title, String description, String image , String field , String readers, String saveIc) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.field = field;
        this.readers = readers;
        this.saveIc = saveIc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getReaders() {
        return readers;
    }

    public void setReaders(String readers) {
        this.readers = readers;
    }

    public String getSaveIc() {
        return saveIc;
    }

    public void setSaveIc(String saveIc) {
        this.saveIc = saveIc;
    }
}
