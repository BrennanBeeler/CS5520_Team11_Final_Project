package edu.neu.madcourse.modernmath.assignments;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Student_Assignment implements Parcelable {

    public String student_email;
    public String time_spent;
    public int num_correct;
    public int num_incorrect;

    public Student_Assignment(String student_email, String time_spent, int num_correct,
                              int num_incorrect)
    {
        this.student_email = student_email;
        this.time_spent = time_spent;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
    }

    public static final Parcelable.Creator<Student_Assignment> CREATOR
            = new Parcelable.Creator<Student_Assignment>() {
        public Student_Assignment createFromParcel(Parcel in) {
            return new Student_Assignment(in);
        }

        public Student_Assignment[] newArray(int size) {
            return new Student_Assignment[size];
        }
    };

    private Student_Assignment(Parcel in) {
        this.student_email = in.readString();
        this.time_spent = in.readString();
        this.num_correct = in.readInt();
        this.num_incorrect = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.student_email);
        out.writeString(this.time_spent);
        out.writeInt(this.num_correct);
        out.writeInt(this.num_incorrect);
    }
}
