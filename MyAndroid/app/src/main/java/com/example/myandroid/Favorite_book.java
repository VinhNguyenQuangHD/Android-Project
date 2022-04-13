package com.example.myandroid;

public class Favorite_book {
    String username,book_name,book_author,book_watch;

    Favorite_book(){}

    public Favorite_book(String username, String book_name, String book_author, String book_watch) {
        this.username = username;
        this.book_name = book_name;
        this.book_author = book_author;
        this.book_watch = book_watch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setBook_watch(String book_watch) {
        this.book_watch = book_watch;
    }

}
