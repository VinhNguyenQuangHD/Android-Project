package com.example.myandroid;

import com.google.android.gms.auth.api.Auth;

public class Author {
    String author_name, author_description, author_img;

    public Author(){}

    public Author(String author_name, String author_description, String author_img) {
        this.author_name = author_name;
        this.author_description = author_description;
        this.author_img = author_img;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_description() {
        return author_description;
    }

    public void setAuthor_description(String author_description) {
        this.author_description = author_description;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }
}
