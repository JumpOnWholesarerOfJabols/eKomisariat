package main.java.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String loginField;
    private String password;

    public User(String id, String nickname, String password) {
        this.id = id;
        this.loginField = nickname;
        this.password = password;
    }

    // Getters for the fields (optional, if needed later)
    public String getId() {
        return id;
    }

    public String getLoginField() {
        return loginField;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Pseudonim: " + loginField + ", Haslo: " + password;
    }
}