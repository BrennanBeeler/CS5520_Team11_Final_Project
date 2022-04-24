package edu.neu.madcourse.modernmath.teacher;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.assignments.Operator;

public class AssignmentListItem {
    private String assignment_id;
    private String title;
    private ArrayList<Operator> operators;
    private String difficulty;
    private int num_questions;
    private int time_limit;

    public AssignmentListItem(String assignment_id, String title, ArrayList<Operator> operators,
                              String difficulty, int num_questions, int time_limit) {
        this.assignment_id = assignment_id;
        this.title = title;
        this.operators = operators;
        this.difficulty = difficulty;
        this.num_questions = num_questions;
        this.time_limit = time_limit;
    }

    public String getAssignment_id()
    {
        return this.assignment_id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Operator> getOperators()
    {
        return this.operators;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getNum_questions() {
        return this.num_questions;
    }

    public int getTime_limit()
    {
        return this.time_limit;
    }
}