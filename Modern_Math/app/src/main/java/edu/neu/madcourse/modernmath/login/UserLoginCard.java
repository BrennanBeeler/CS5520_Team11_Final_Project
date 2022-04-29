package edu.neu.madcourse.modernmath.login;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.madcourse.modernmath.database.User;

public class UserLoginCard extends AppCompatActivity {

    public String username;

    public String firstName;

    public String lastName;

    public int age;

    public boolean active;

    public boolean is_teacher;

    public String class_code;

    public int answers;

    public UserLoginCard() {}

    public UserLoginCard(User user)
    {
        this.username = user.username;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.age = user.age;
        this.active = user.active;
        this.is_teacher = user.is_teacher;
        this.class_code = user.class_code;
        this.answers = user.answers;
    }

    public String getName()
    {
        return this.firstName + " " + this.lastName;
    }
}