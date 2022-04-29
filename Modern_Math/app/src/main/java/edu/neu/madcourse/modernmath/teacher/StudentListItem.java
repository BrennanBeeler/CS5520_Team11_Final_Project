package edu.neu.madcourse.modernmath.teacher;

public class StudentListItem {
    private String name;
    private String username;

    public StudentListItem(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
