package com.abdelrahman.irihackathon.Model;

public class Answer {
    private String answer, rating, addedBy, addedOn, answerID;

    public Answer(){
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswerID() {
        return answerID;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public String getAnswer() {
        return answer;
    }

    public String getRating() {
        return rating;
    }
}
