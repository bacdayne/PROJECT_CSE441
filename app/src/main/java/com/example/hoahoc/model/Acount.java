package com.example.hoahoc.model;

import java.util.List;

public class Acount {
    String name, email, username, password;
    List<String> role;

    public Acount() {
    }
    public Acount(String name, String email, String username, String password, List <String> role) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getRole() {
        return role;
    }
    public void setRole(List<String> role) {
        this.role = role;
    }

}
