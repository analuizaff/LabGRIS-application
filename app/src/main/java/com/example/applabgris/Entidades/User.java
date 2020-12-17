package com.example.applabgris.Entidades;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private String keyUser;
    private String password;
    public Map<String, Boolean> users = new HashMap<>();

    public User(){}

    public User(String name, String email, String keyUser){
        this.name = name;
        this.email = email;
        this.keyUser = keyUser;
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

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("keyUser", keyUser);

        result.put("users", users);

        return result;
    }
}
