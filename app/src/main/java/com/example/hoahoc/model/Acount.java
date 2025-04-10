package com.example.hoahoc.model;

public class Acount {
    private String uid;
    private String name;
    private String email;
    private String username;
    private String password;
    private String role;

    // Constructor mặc định bắt buộc cho Firebase
    public Acount() {
    }

    // Constructor đầy đủ
    public Acount(String uid, String name, String email, String username, String password, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter & Setter cho uid
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Getter & Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter & Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter & Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter & Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter & Setter cho role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
