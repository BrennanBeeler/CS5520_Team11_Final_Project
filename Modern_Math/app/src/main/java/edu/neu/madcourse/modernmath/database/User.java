package edu.neu.madcourse.modernmath.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User implements Parcelable {
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.userID);
        out.writeString(this.firstName);
        out.writeString(this.lastName);
        out.writeInt(this.age);
        // Cannot use writeBoolean with API level of 26
        out.writeInt(this.active ? 1 : 0);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        this.userID = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.age = in.readInt();
        // Cannot use readBoolean with API level of 26
        this.active = in.readInt() == 1;
    }

    @Override
    public String toString()
    {
        return "UserID: " + this.userID +
                "\nFirstName: " + this.firstName +
                "\nLastName: " + this.lastName +
                "\nAge: " + this.age +
                "\nActive: " + this.active;
    }
}
