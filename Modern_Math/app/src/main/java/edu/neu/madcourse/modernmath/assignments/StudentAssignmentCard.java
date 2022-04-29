package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.AppCompatActivity;


public class StudentAssignmentCard extends AppCompatActivity {

    public String student_name;
    public String student_username;
    public int time_spent;
    public int num_correct;
    public int num_incorrect;
    private boolean completion_status;

    public StudentAssignmentCard() {}

    public StudentAssignmentCard(String student_username, int time_spent, int num_correct, int num_incorrect)
    {
        this.student_username = student_username;
        this.time_spent = time_spent;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
        this.completion_status = false;
    }

    public StudentAssignmentCard(String student_username, int time_spent, int num_correct, int num_incorrect, String student_name)
    {
        this.student_username = student_username;
        this.time_spent = time_spent;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
        this.student_name = student_name;
        this.completion_status = false;
    }

    public String getUsername()
    {
        return this.student_username;
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

    public void setCompletion_status(boolean completion_status) {
        this.completion_status = completion_status;
    }

    public boolean isComplete() {
        return completion_status;
    }
}