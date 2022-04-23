package edu.neu.madcourse.modernmath.assignments;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.neu.madcourse.modernmath.database.User;

public class Assignment implements Parcelable {
    public String assignment_id;

    public String assignment_title;

    public ArrayList<Operator> operators;

    public Difficulty difficulty;

    public int time;

    public int num_questions;

    public ArrayList<Student_Assignment> student_assignments;

    public Assignment(String assignment_id, String assignment_title, Difficulty difficulty,
                      ArrayList<Operator> operators, int time, int num_questions,
                      ArrayList<Student_Assignment> student_assignments)
    {
        this.assignment_id = Objects.requireNonNull(assignment_id, "assignment_id must not be null");
        this.assignment_title = Objects.requireNonNull(assignment_title, "assignment_title must not be null");
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty must not be null");
        this.operators = Objects.requireNonNull(operators, "operators must not be null");
        this.time = time;
        this.num_questions = num_questions;
        this.student_assignments = student_assignments;
    }

    public static final Parcelable.Creator<Assignment> CREATOR
            = new Parcelable.Creator<Assignment>() {
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    private Assignment(Parcel in) {
        this.assignment_id = in.readString();
        this.assignment_title = in.readString();
        this.operators = in.readArrayList(null);
        this.difficulty = Difficulty.valueOf(in.readString());
        this.time = in.readInt();
        this.num_questions = in.readInt();

        this.student_assignments = new ArrayList<>();

        for (Parcelable p : in.readParcelableArray(getClass().getClassLoader()))
        {
            this.student_assignments.add((Student_Assignment) p);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.assignment_id);
        out.writeString(this.assignment_title);
        out.writeList(this.operators);
        out.writeString(String.valueOf(this.difficulty));
        out.writeInt(this.time);
        out.writeInt(this.num_questions);

        Student_Assignment[] test = new Student_Assignment[this.student_assignments.size()];

        out.writeParcelableArray(this.student_assignments.toArray(test), flags);
    }

    @NonNull
    @Override
    public String toString()
    {
        return "\nAssignmentID: " + this.assignment_id +
                "\nAssignment Title: " + this.assignment_title +
                "\nOperators: " + this.operators +
                "\nDifficulty: " + this.difficulty +
                "\nTime: " + this.time +
                "\nNum Qs: " + this.num_questions +
                "\nNum students: " + this.student_assignments;
    }
}