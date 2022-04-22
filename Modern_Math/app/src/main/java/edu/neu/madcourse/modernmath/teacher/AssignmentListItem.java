package edu.neu.madcourse.modernmath.teacher;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.assignments.Operator;

public class AssignmentListItem {
    private String title;
    private ArrayList<Operator> operators;
    private String difficulty;
    private String num_questions;
    private String time_limit;

    public AssignmentListItem(String title, ArrayList<Operator> operators, String difficulty, String num_questions, String time_limit) {
        this.title = title;
        this.operators = operators;
        this.difficulty = difficulty;
        this.num_questions = num_questions;
        this.time_limit = time_limit;
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

    public String getNum_questions() {
        return this.num_questions;
    }

    public String getTime_limit()
    {
        return this.time_limit;
    }
}
