package edu.neu.madcourse.modernmath.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @NonNull
    @PrimaryKey
    public String userID;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "age")
    public int age;

    @ColumnInfo(name = "active")
    public boolean active;

    public User(@NonNull String userID, String firstName, String lastName, int age, boolean active)
    {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.active = active;
    }

    public User() {

    }
}
