package edu.neu.madcourse.modernmath.problem_screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Difficulty;
import edu.neu.madcourse.modernmath.assignments.Operator;

public class ProblemScreenActivity extends AppCompatActivity {

    private String userAnswer = "";
    private Difficulty difficulty = Difficulty.EASY;
    private int numOfQuestions = 10;
    private long time = 0;
    private int operand1;
    private int operand2;
    private int answer;
    private String assignmentCode;
    private String userId = "test2";
    private Operator[] operators = {Operator.ADDITION, Operator.SUBTRACTION, Operator.MULTIPLICATION, Operator.DIVISION};
    private int correctAnswers = 0;
    private int incorrectAnswer = 0;
    EditText answerField;
    TextView numberOfQuestionsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            difficulty = (Difficulty) extras.getSerializable("difficulty");
            numOfQuestions = extras.getInt("numberOfQuestions");
            time = extras.getLong("time");
            assignmentCode = extras.getString("assignmentCode");
            userId = extras.getString("userId");
            operators = (Operator[]) extras.getSerializable("operators");
        }
        if (operators.length == 0 || userId.isEmpty()) {
            Toast.makeText(getBaseContext(), getString(R.string.error),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        Dialog dialog = showStartDialog();
        dialog.show();
    }

    private void init(){
            answerField = findViewById(R.id.answerText);
            answerField.setShowSoftInputOnFocus(false);
            answerField.setCursorVisible(false);

            if(numOfQuestions > 0) {
                numberOfQuestionsField = findViewById(R.id.numberofQuestions);
                String text = String.format(getString(R.string.number_of_Questions),
                        numOfQuestions);
                numberOfQuestionsField.setText(text);
            }
            TextView timer = findViewById(R.id.timer);

            generateQuestion();
            if (time > 0) {
                new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long l) {
                        String timeString = "Time Remaining: " + l / 1000;
                        timer.setText(timeString);
                    }

                    @Override
                    public void onFinish() {
                        showEndDialog("Times up!\n").show();
                    }
                }.start();
            }
    }

    private Dialog showStartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String message = getString(R.string.start_challenge) + '\n';
        if(numOfQuestions > 0) {
            message = message + '\n' + String.format(getString(R.string.number_of_Questions),
                    numOfQuestions);
        }
        if (time > 0) {
            message = message + '\n' + String.format(getString(R.string.time),
                    String.valueOf(time / 1000));
        }

         builder.setMessage(message)
                .setPositiveButton(R.string.start_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        init();
                    }
                });
         builder.setCancelable(false);
         return builder.create();
    }

    private Dialog showEndDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String text = "";
        if(!s.isEmpty()) {
            text = s;
        }
        text = text + String.format(getString(R.string.correct_answers), correctAnswers) + '\n' +
                String.format(getString(R.string.incorrect_answers), incorrectAnswer);

        builder.setMessage(text)
                .setPositiveButton(R.string.home, (dialogInterface, i) -> {
                    finish();
                });
        builder.setCancelable(false);
        return builder.create();
    }

    public void numberPressed(View view) {
        TextView textView = (TextView) view;
        String number = textView.getText().toString();
        if(getUserAnswer().isEmpty()) {
            setUserAnswer(number);
        } else {
            setUserAnswer(getUserAnswer() + number);
        }
        answerField.setText(getUserAnswer());
    }

    public void clear(View view) {
        if(!getUserAnswer().isEmpty()) {
            setUserAnswer(getUserAnswer().substring(0, getUserAnswer().length() - 1));
            answerField.setText(getUserAnswer());
        }
    }

    public void submit(View view) {
        if (!getUserAnswer().isEmpty()) {
            if (getUserAnswer().equals(String.valueOf(getAnswer()))) {
                correctAnswers++;
                Snackbar snackbar = Snackbar.make(findViewById(R.id.problem_screen),"Correct Answer",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint( getResources().getColor(R.color.purple_200));
                snackbar.show();
            } else {
                incorrectAnswer++;
                Snackbar snackbar = Snackbar.make(findViewById(R.id.problem_screen),"Incorrect Answer",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint( getResources().getColor(R.color.cardview_dark_background));
                snackbar.show();
            }
            --numOfQuestions;
            if (numOfQuestions > 0) {
                String text = String.format(getString(R.string.number_of_Questions),
                        numOfQuestions);
                numberOfQuestionsField.setText(text);
            } else if (numOfQuestions == 0) {
                showEndDialog("").show();
            }
            setUserAnswer("");
            answerField.setText("");
            generateQuestion();
        }
    }

    private void generateQuestion() {

        int selectedOpIndex = generateRandom(operators.length, 0);
        Operator selectedOp = operators[selectedOpIndex];

        switch (selectedOp) {
            case ADDITION: generateAdd();
                break;
            case SUBTRACTION: generateSub();
                break;
            case MULTIPLICATION: generateMulti();
                break;
            default: generateDiv();
        }
        TextView op1 = findViewById(R.id.operand_1);
        TextView op2 = findViewById(R.id.operand_2);
        op1.setText(String.valueOf(getOperand1()));
        op2.setText(String.valueOf(getOperand2()));

        TextView op = findViewById(R.id.operator);
        op.setText(String.valueOf(selectedOp.getValue()));
        setAnswer(selectedOp);
    }

    private void generateAdd() {
        switch (difficulty) {
            case EASY: setOperand1(generateRandom(10,0));
                setOperand2(generateRandom(10,0));
                break;
            case MEDIUM: setOperand1(generateRandom(90,10));
                setOperand2(generateRandom(90, 10));
                break;
            default: setOperand1(generateRandom(900, 100));
                setOperand2(generateRandom(900, 100));
        }
    }

    private void generateSub() {
        switch (difficulty) {
            case EASY: setOperand1(generateRandom(10,0));
                setOperand2(generateRandom(10,0));
                break;
            case MEDIUM: setOperand1(generateRandom(90,10));
                setOperand2(generateRandom(90, 10));
                break;
            default: setOperand1(generateRandom(900, 100));
                setOperand2(generateRandom(900, 100));
        }
        if(getOperand1() < getOperand2()) {
            generateSub();
        }
    }

    private void generateMulti() {
        switch (difficulty) {
            case EASY: setOperand1(generateRandom(10,1));
                break;
            case MEDIUM: setOperand1(generateRandom(20,10));
                break;
            default: setOperand1(generateRandom(30, 20));
        }
        setOperand2(generateRandom(10,1));
    }

    private void generateDiv() {
        switch (difficulty) {
            case EASY: setOperand1(generateRandom(100,0));
                break;
            case MEDIUM: setOperand1(generateRandom(500,100));
                break;
            default: setOperand1(generateRandom(1000, 500));
        }
        setOperand2(generateRandom(9,2));
        if(getOperand1() % getOperand2() != 0 || getOperand1() < getOperand2()) {
            generateDiv();
        }
    }

    private void setAnswer(Operator operator) {
        switch (operator) {
            case ADDITION:
                setAnswer(getOperand1() + getOperand2());
                break;
            case SUBTRACTION: setAnswer(getOperand1() - getOperand2());
                break;
            case MULTIPLICATION: setAnswer(getOperand1() * getOperand2());
                break;
            default : setAnswer(getOperand1() / getOperand2());
        }
    }

    private int generateRandom(int bound, int offset) {
        Random random = new Random();
        return random.nextInt(bound) + offset;
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