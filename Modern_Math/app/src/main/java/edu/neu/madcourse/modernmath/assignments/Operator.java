package edu.neu.madcourse.modernmath.assignments;

public enum Operator {
    ADDITION('+'), SUBTRACTION('-'), MULTIPLICATION('*'), DIVISION('/');

    public char value;

    Operator(char c) {
        this.value = c;
    }

    public char getValue() {
        return value;
    }
}
