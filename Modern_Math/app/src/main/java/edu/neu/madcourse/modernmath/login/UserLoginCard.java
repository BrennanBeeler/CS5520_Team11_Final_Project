package edu.neu.madcourse.modernmath.login;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.madcourse.modernmath.R;

public class UserLoginCard extends AppCompatActivity {

    private String username;

    public UserLoginCard() {}

    public UserLoginCard(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return this.username;
    }
}