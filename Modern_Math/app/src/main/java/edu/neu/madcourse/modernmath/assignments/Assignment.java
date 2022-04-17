package edu.neu.madcourse.modernmath.assignments;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

public class Assignment {
    public String assignment_id;

    public String assignment_title;

    public ArrayList<Operator> operators;

    public Difficulty difficulty;

    public Duration time;

    public int num_questions;

    public Assignment(String assignment_id, String assignment_title, Difficulty difficulty,
                      ArrayList<Operator> operators, Duration time, int num_questions)
    {
        this.assignment_id = Objects.requireNonNull(assignment_id, "assignment_id must not be null");
        this.assignment_title = Objects.requireNonNull(assignment_title, "assignment_title must not be null");
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty must not be null");
        this.operators = Objects.requireNonNull(operators, "operators must not be null");
        this.time = time;
        this.num_questions = num_questions;
    }

}
