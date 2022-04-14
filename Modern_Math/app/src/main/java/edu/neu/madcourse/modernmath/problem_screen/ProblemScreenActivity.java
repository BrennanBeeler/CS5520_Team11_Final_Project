package edu.neu.madcourse.modernmath.problem_screen;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import edu.neu.madcourse.modernmath.R;

public class ProblemScreenActivity extends AppCompatActivity {

    private String userAnswer = "";
    private int difficulty = 2;
    private int numOfQuestions;
    private long time;
    private char operator = '+';
    private int operand1;
    private int operand2;
    private int answer;
    EditText answerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            difficulty = extras.getInt("difficulty");
            numOfQuestions = extras.getInt("numberOfQuestions");
            time = extras.getLong("time");
            operator = extras.getChar("operator");
        }
        answerField = findViewById(R.id.answerText);
        answerField.setShowSoftInputOnFocus(false);
        answerField.setCursorVisible(false);
        if (operator ==	'\u0000' || difficulty == 0) {
            //TO-DO: show snackBar
        } else {
            TextView op = findViewById(R.id.operator);
            op.setText("+");
            generateQuestion();
        }
    }

    public void numberPressed(View view) {
        TextView textView = (TextView) view;
        String number = textView.getText().toString();
        if(userAnswer.isEmpty()) {
            setUserAnswer(number);
        } else {
            setUserAnswer(getUserAnswer() + number);
        }
        answerField.setText(userAnswer);
    }

    public void clear(View view) {
        if(!userAnswer.isEmpty()) {
            setUserAnswer(getUserAnswer().substring(0, getUserAnswer().length() - 1));
            answerField.setText(getUserAnswer());
        }
    }

    public void submit(View view) {
        if (!userAnswer.isEmpty()) {
            if (userAnswer.equals(String.valueOf(answer))) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.problem_screen),"Correct Answer",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint( getResources().getColor(R.color.purple_200));
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.problem_screen),"Incorrect Answer",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint( getResources().getColor(R.color.cardview_dark_background));
                snackbar.show();
            }
            setUserAnswer("");
            answerField.setText("");
            generateQuestion();
        }
    }

    private void generateQuestion() {
        Random random = new Random();
        switch (difficulty) {
            case 1 :
                setOperand1(random.nextInt(10));
                setOperand2(random.nextInt(10));
                break;
            case 2 :
                setOperand1(random.nextInt(90)+10);
                setOperand2(random.nextInt(90)+10);
                break;
            default: setOperand1(random.nextInt(900) + 100);
                setOperand2(random.nextInt(900) + 100);
        }
        TextView op1 = findViewById(R.id.operand_1);
        TextView op2 = findViewById(R.id.operand_2);
        op1.setText(String.valueOf(getOperand1()));
        op2.setText(String.valueOf(getOperand2()));
        setAnswer();
    }

    private void setAnswer() {
        switch (operator) {
            case '+':
                setAnswer(getOperand1() + getOperand2());
                break;
            case '-': setAnswer(getOperand1() - getOperand2());
                break;
            case '*': setAnswer(getOperand1() * getOperand2());
                break;
            default : setAnswer(getOperand1() / getOperand2());
        }
    }


    public int getOperand1() {
        return operand1;
    }

    public void setOperand1(int operand1) {
        this.operand1 = operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperand2(int operand2) {
        this.operand2 = operand2;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}