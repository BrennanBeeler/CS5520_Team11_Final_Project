package edu.neu.madcourse.modernmath.login;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.madcourse.modernmath.R;

public class UserLoginCard extends AppCompatActivity {

    private String name;

    public UserLoginCard() {}

    public UserLoginCard(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}