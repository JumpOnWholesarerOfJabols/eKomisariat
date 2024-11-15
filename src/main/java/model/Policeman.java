package main.java.model;

public class Policeman extends User{
    private Department department;

    public Policeman(String name, String surname, String email, String password, Department department) {
        super(name, surname, email, password);
        this.department = department;
    }
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}