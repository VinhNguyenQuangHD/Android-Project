package com.example.myandroid;

public class Searchingpage_title_list {
    int Anh_chu_de,Anh_chu_de2;
    String ten_chu_de, ten_chu_de2;

    public Searchingpage_title_list(int anh_chu_de, int anh_chu_de2,
                                    String ten_chu_de, String ten_chu_de2)
    {
        Anh_chu_de = anh_chu_de;
        Anh_chu_de2 = anh_chu_de2;
        this.ten_chu_de = ten_chu_de;
        this.ten_chu_de2 = ten_chu_de2;
    }

    public int getAnh_chu_de() {
        return Anh_chu_de;
    }

    public void setAnh_chu_de(int anh_chu_de) {
        Anh_chu_de = anh_chu_de;
    }

    public int getAnh_chu_de2() {
        return Anh_chu_de2;
    }

    public void setAnh_chu_de2(int anh_chu_de2) {
        Anh_chu_de2 = anh_chu_de2;
    }

    public String getTen_chu_de() {
        return ten_chu_de;
    }

    public void setTen_chu_de(String ten_chu_de) {
        this.ten_chu_de = ten_chu_de;
    }

    public String getTen_chu_de2() {
        return ten_chu_de2;
    }

    public void setTen_chu_de2(String ten_chu_de2) {
        this.ten_chu_de2 = ten_chu_de2;
    }
}
