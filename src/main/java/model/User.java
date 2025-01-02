package main.java.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String surname;
    private String email;
    private String password;

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    public String getName() {
        return name;
    }


    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "imie: " + name + " nazwisko: " + surname + " email: " + email + ", Haslo: " + password;
    }
}