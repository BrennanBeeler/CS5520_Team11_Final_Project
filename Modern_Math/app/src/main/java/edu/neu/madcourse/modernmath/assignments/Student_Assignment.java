package edu.neu.madcourse.modernmath.assignments;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Student_Assignment extends Assignment{

    public String student_email;
    public Date started;
    public Date completed;
    public int num_correct;
    public int num_incorrect;

    public Student_Assignment(String assignment_id, String assignment_title, Difficulty difficulty,
                              ArrayList<Operator> operators, Duration time, int num_questions,
                              String student_email, Date started, Date completed, int num_correct,
                              int num_incorrect) {
        super(assignment_id, assignment_title, difficulty, operators, time, num_questions);
        this.student_email = student_email;
        this.started = started;
        this.completed = completed;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
    }

    public Student_Assignment(Assignment assignment, String student_email, Date started,
                              Date completed, int num_correct, int num_incorrect)
    {
        super(assignment.assignment_id, assignment.assignment_title, assignment.difficulty,
                assignment.operators, assignment.time, assignment.num_questions);
        this.student_email = student_email;
        this.started = started;
        this.completed = completed;
        this.num_correct = num_correct;
        this.num_incorrect = num_incorrect;
    }
}
