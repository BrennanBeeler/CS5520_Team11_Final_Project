package edu.neu.madcourse.modernmath.assignments;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.madcourse.modernmath.R;

public class StudentAssignmentCard extends AppCompatActivity {

    public String student_email;
    public String time_spent;
    public int num_correct;
    public int num_incorrect;

    public StudentAssignmentCard() {}

    public StudentAssignmentCard(Student_Assignment student_assignment)
    {
        this.student_email = student_assignment.student_email;
        this.time_spent = student_assignment.time_spent;
        this.num_correct = student_assignment.num_correct;
        this.num_incorrect = student_assignment.num_incorrect;
    }

    public String getEmail()
    {
        return this.student_email;
    }

    public String getTimeSpent()
    {
        return this.time_spent;
    }

    public int getNumCorrect()
    {
        return this.num_correct;
    }

    public int getNumIncorrect()
    {
        return this.num_incorrect;
    }
}