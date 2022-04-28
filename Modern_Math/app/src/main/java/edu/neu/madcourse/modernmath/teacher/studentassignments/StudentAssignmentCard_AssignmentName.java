package edu.neu.madcourse.modernmath.teacher.studentassignments;

import androidx.appcompat.app.AppCompatActivity;



public class StudentAssignmentCard_AssignmentName extends AppCompatActivity {

    public String assignment_name;
    public int time_spent;
    public int num_correct;
    public int num_incorrect;
    private boolean completion_status;

    public StudentAssignmentCard_AssignmentName() {}

    public StudentAssignmentCard_AssignmentName(String assignment_name, int time_spent, int num_correct, int num_incorrect)
    {
        this.assignment_name = assignment_name;
        this.time_spent = time_spent;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
        this.completion_status = false;
    }

    public String getAssignment_name()
    {
        return this.assignment_name;
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

    public boolean isCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(boolean completion_status) {
        this.completion_status = completion_status;
    }
}