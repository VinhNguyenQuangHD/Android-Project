package com.example.myandroid;

public class Book_chapter {
    String book_name,book_chapter,book_chapter_content;

    public Book_chapter(){}

    public Book_chapter(String book_chapter, String book_chapter_content, String book_name) {
        this.book_name = book_name;
        this.book_chapter = book_chapter;
        this.book_chapter_content = book_chapter_content;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_chapter() {
        return book_chapter;
    }

    public void setBook_chapter(String book_chapter) {
        this.book_chapter = book_chapter;
    }

    public String getBook_chapter_content() {
        return book_chapter_content;
    }

    public void setBook_chapter_content(String book_chapter_content) {
        this.book_chapter_content = book_chapter_content;
    }
}
