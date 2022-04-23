package edu.neu.madcourse.modernmath.teacher;

public class StudentListItem {
    private String name;
    private String email;

    public StudentListItem(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
