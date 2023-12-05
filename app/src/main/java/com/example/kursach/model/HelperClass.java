package com.example.kursach.model;

public class HelperClass {

    String name, email, username, hashedPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public HelperClass(String name, String email, String username, String hashedPassword) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public HelperClass() {
    }
}
