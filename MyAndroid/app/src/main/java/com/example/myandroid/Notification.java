package com.example.myandroid;

public class Notification {
    String from,to,content,type;

    public Notification(){}

    public Notification(String from, String to, String content,String type) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
