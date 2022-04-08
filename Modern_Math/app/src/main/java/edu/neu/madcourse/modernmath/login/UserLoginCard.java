package edu.neu.madcourse.modernmath.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

public class UserLoginCard extends AppCompatActivity {

    public String email;

    public String firstName;

    public String lastName;

    public int age;

    public boolean active;

    public boolean is_teacher;

    public UserLoginCard() {}

    public UserLoginCard(User user)
    {
        this.email = user.email;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.age = user.age;
        this.active = user.active;
        this.is_teacher = user.is_teacher;
    }

    public String getName()
    {
        return this.firstName + " " + this.lastName;
    }
}