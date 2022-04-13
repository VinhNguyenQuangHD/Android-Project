package com.example.myandroid;

public class Library {
    String email,lib_name,lib_description;

    public Library(){}

    public Library(String email, String lib_name, String lib_description) {
        this.email = email;
        this.lib_name = lib_name;
        this.lib_description = lib_description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLib_name() {
        return lib_name;
    }

    public void setLib_name(String lib_name) {
        this.lib_name = lib_name;
    }

    public String getLib_description() {
        return lib_description;
    }

    public void setLib_description(String lib_description) {
        this.lib_description = lib_description;
    }
}
