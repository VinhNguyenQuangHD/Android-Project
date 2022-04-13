package com.example.myandroid;

public class Book_history {
    String email,book_name, time;

    Book_history(){}

    public Book_history(String email, String book_name, String time) {
        this.email = email;
        this.book_name = book_name;
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
