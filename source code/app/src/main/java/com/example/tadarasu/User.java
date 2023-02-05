package com.example.tadarasu;


public class User {

    public String name, email;

    public User() {

    }
    public User(String flame, String email) {
        this.name = flame;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}