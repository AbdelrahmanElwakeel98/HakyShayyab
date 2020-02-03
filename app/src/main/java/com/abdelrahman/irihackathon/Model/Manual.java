package com.abdelrahman.irihackathon.Model;

public class Manual {
    private String title, description, categoryID, addedBy, media, blogID, location;

    public Manual(){
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getBlogID() {
        return blogID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setBlogID(String blogID) {
        this.blogID = blogID;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getDescription() {
        return description;
    }

    public String getMedia() {
        return media;
    }

    public String getTitle() {
        return title;
    }
}
