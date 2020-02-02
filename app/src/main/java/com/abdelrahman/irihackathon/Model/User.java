package com.abdelrahman.irihackathon.Model;

public class User {
    private String username, phone, tribe;

    public User(){
    }

    public User(String username, String phone){
        this.username = username;
        this.phone = phone;
    }

    public User(String username, String phone, String tribe){
        this.username = username;
        this.phone = phone;
        this.tribe = tribe;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getTribe() {
        return tribe;
    }
}
