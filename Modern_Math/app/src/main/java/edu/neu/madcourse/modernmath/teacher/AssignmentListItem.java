package edu.neu.madcourse.modernmath.teacher;

public class AssignmentListItem {
    private boolean add;
    private boolean subtract;
    private boolean multiply;
    private boolean divide;

    private String title;
    private String difficulty;
    private int numQuestions;

    public AssignmentListItem(boolean add, boolean subtract, boolean multiply, boolean divide, String title, String difficulty, int numQuestions) {
        this.add = add;
        this.subtract = subtract;
        this.multiply = multiply;
        this.divide = divide;
        this.title = title;
        this.difficulty = difficulty;
        this.numQuestions = numQuestions;
    }

    public boolean isAdd() {
        return add;
    }

    public boolean isSubtract() {
        return subtract;
    }

    public boolean isMultiply() {
        return multiply;
    }

    public boolean isDivide() {
        return divide;
    }

    public String getTitle() {
        return title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getNumQuestions() {
        return numQuestions;
    }
}
