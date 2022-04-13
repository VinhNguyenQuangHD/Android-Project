package com.example.myandroid;

public class Ranking {
    String username,book_name,rank_point;

    public Ranking(){}

    public Ranking(String username, String book_name, String rank_point) {
        this.username = username;
        this.book_name = book_name;
        this.rank_point = rank_point;
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

    public String getRank_point() {
        return rank_point;
    }

    public void setRank_point(String rank_point) {
        this.rank_point = rank_point;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "username='" + username + '\'' +
                ", book_name='" + book_name + '\'' +
                ", rank_point='" + rank_point + '\'' +
                '}';
    }
}
