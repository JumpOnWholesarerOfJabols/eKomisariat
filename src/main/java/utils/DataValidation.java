package main.java.utils;

public class DataValidation {

    public static boolean isNameValid(String name) {
        return name.matches("[A-Z].{2,}");
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") && !email.contains("@eKomisariat.pl");
    }

    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$");
    }
}
