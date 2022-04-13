package com.example.myandroid;

public class Lib_detail {
    String lib_name,book_name,book_author,book_watch,email;

    public Lib_detail(){}

    public Lib_detail(String lib_name, String book_name, String book_author, String book_watch, String email) {
        this.lib_name = lib_name;
        this.book_name = book_name;
        this.book_author = book_author;
        this.book_watch = book_watch;
        this.email = email;
    }

    public String getLib_name() {
        return lib_name;
    }

    public void setLib_name(String lib_name) {
        this.lib_name = lib_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_watch() {
        return book_watch;
    }

    public void setBook_watch(String book_view) {
        this.book_watch = book_view;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
