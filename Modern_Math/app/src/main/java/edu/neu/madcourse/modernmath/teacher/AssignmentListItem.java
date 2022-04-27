package edu.neu.madcourse.modernmath.teacher;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.assignments.Operator;

public class AssignmentListItem {
    private String assignment_id;
    private String title;
    private ArrayList<Operator> operators;
    private String difficulty;
    private int num_questions;
    private long time_limit;
    private boolean completion_status;

    public AssignmentListItem(String assignment_id, String title, ArrayList<Operator> operators,
                              String difficulty, int num_questions, int time_limit) {
        this.assignment_id = assignment_id;
        this.title = title;
        this.operators = operators;
        this.difficulty = difficulty;
        this.num_questions = num_questions;
        this.time_limit = time_limit;
        this.completion_status = false;
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

    public long getTime_limit()
    {
        return this.time_limit;
    }


    public boolean isCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(boolean completion_status) {
        this.completion_status = completion_status;
    }
}
