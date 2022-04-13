package com.example.myandroid;

public class Friend_req {
    String username,email_send,email;

    public Friend_req(){}

    public Friend_req(String username, String email_send, String email) {
        this.username = username;
        this.email_send = email_send;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail_send() {
        return email_send;
    }

    public void setEmail_send(String email_send) {
        this.email_send = email_send;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
