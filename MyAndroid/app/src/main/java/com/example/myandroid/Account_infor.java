package com.example.myandroid;

public class Account_infor {
    String username,point,email,imgsrc,acc_type,description;

    public Account_infor(){}

    public Account_infor(String username, String point, String email, String imgsrc, String acc_type, String description) {
        this.username = username;
        this.point = point;
        this.email = email;
        this.imgsrc = imgsrc;
        this.acc_type = acc_type;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
