package edu.neu.madcourse.modernmath.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import edu.neu.madcourse.modernmath.login.UserLoginCard;

@Entity(tableName = "users")
public class User implements Parcelable {
    @NonNull
    @PrimaryKey
    public String email;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "age")
    public int age;

    @ColumnInfo(name = "active")
    public boolean active;

    @ColumnInfo(name = "is_teacher")
    public boolean is_teacher;

    public User(@NonNull String email, String firstName, String lastName, int age, boolean active,
                boolean is_teacher)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.active = active;
        this.is_teacher = is_teacher;
    }

    public User() {

    }

    @Ignore
    public User(UserLoginCard userLoginCard)
    {
        this.email = userLoginCard.email;
        this.firstName = userLoginCard.firstName;
        this.lastName = userLoginCard.lastName;
        this.age = userLoginCard.age;
        this.active = userLoginCard.active;
        this.is_teacher = userLoginCard.is_teacher;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.email);
        out.writeString(this.firstName);
        out.writeString(this.lastName);
        out.writeInt(this.age);
        // Cannot use writeBoolean with API level of 26
        out.writeInt(this.active ? 1 : 0);
        out.writeInt(this.is_teacher ? 1 : 0);
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
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.age = in.readInt();
        // Cannot use readBoolean with API level of 26
        this.active = in.readInt() == 1;
        this.is_teacher = in.readInt() == 1;
    }

    @Override
    public String toString()
    {
        return "\nUserID: " + this.email +
                "\nFirst Name: " + this.firstName +
                "\nLast Name: " + this.lastName +
                "\nAge: " + this.age +
                "\nActive: " + this.active +
                "\nIs teacher: " + this.is_teacher;
    }
}
