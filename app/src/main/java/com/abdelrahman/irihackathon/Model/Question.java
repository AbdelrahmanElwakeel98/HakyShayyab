package com.abdelrahman.irihackathon.Model;

public class Question {
    private String addedBy, addedOn, categoryId, isEdited,
            question, id;

    public Question(){
    }

    public Question(String addedBy, String addedOn, String categoryId, String question, String id){
        this.addedBy = addedBy;
        this.addedOn = addedOn;
        this.categoryId = categoryId;
        this.question = question;
        this.id = id;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setIsEdited(String isEdited) {
        this.isEdited = isEdited;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getIsEdited() {
        return isEdited;
    }

    public String getQuestion() {
        return question;
    }
}
