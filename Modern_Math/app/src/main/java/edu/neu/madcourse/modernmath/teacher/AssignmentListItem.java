package edu.neu.madcourse.modernmath.teacher;

public class AssignmentListItem {
    private boolean add;
    private boolean subtract;
    private boolean multiply;
    private boolean divide;

    private String title;
    private String difficulty;
    private String amount;

    public boolean isTimed;

    public AssignmentListItem(boolean add, boolean subtract, boolean multiply, boolean divide, String title, String difficulty, String amount, boolean isTimed) {
        this.add = add;
        this.subtract = subtract;
        this.multiply = multiply;
        this.divide = divide;
        this.title = title;
        this.difficulty = difficulty;
        this.amount = amount;
        this.isTimed = isTimed;
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

    public String getAmount() {
        return amount;
    }
}
