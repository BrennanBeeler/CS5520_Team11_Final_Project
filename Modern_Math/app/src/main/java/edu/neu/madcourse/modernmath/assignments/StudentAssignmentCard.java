package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.AppCompatActivity;


public class StudentAssignmentCard extends AppCompatActivity {

    public String student_name;
    public String student_email;
    public int time_spent;
    public int num_correct;
    public int num_incorrect;

    public StudentAssignmentCard(String student_email, int time_spent, int num_correct, int num_incorrect)
    {
        this.student_email = student_email;
        this.time_spent = time_spent;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
    }

    public StudentAssignmentCard(String student_email, int time_spent, int num_correct, int num_incorrect, String student_name)
    {
        this.student_email = student_email;
        this.time_spent = time_spent;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
        this.student_name = student_name;
    }

    public String getEmail()
    {
        return this.student_email;
    }

    public int getTimeSpent()
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