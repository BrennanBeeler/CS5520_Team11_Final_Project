package edu.neu.madcourse.modernmath.problemselection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Difficulty;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.database.User;

import edu.neu.madcourse.modernmath.studentassignments.StudentAssignmentsActivity;

import edu.neu.madcourse.modernmath.problem_screen.ProblemScreenActivity;


/**
 * Class that displays various input options to player to start the game.
 */
public class ProblemSelectionActivity extends AppCompatActivity {

    private Difficulty selectedPlayLevel;
    private ArrayList<Operator> selectedPlayMode = new ArrayList<>();
    private AlertDialog.Builder missingInputAlert;
    private TextView activeUser;
    private SwitchCompat add_switch;
    private SwitchCompat sub_switch;
    private SwitchCompat multi_switch;
    private SwitchCompat div_switch;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_problem);

        // Welcome Message
        activeUser = findViewById(R.id.activeUserTextView);
        if (getIntent().getExtras() != null)
        {
            user = getIntent().getExtras().getParcelable("active_user");
            activeUser.setText("Welcome " + user.firstName);
        }


        // Mode Selection - Multiple Selection
        add_switch = findViewById(R.id.addition_switch);
        sub_switch = findViewById(R.id.subtraction_switch);
        multi_switch = findViewById(R.id.multiplication_switch);
        div_switch = findViewById(R.id.division_switch);


        // Level Selection - Default selection as easy
        Spinner levelSpinner = (Spinner) findViewById(R.id.levelSpinner);
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter
                .createFromResource(this, R.array.level_array,
                        android.R.layout.simple_spinner_item);
        levelAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);

        levelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedPlayLevel = Difficulty.valueOf(String.valueOf(parent.getItemAtPosition(position)));
                Log.v("Difficulty level ", selectedPlayLevel.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }


    public void startPracticeSession(View view) {
        if (!validateInputs()) {
            // TODO: Change of Destination
            Intent practiceIntent = new Intent(ProblemSelectionActivity.this, ProblemScreenActivity.class);
            practiceIntent.putExtra("active_user", user);
            practiceIntent.putExtra("play_mode", this.selectedPlayMode);
            practiceIntent.putExtra("play_level", this.selectedPlayLevel);
            startActivity(practiceIntent);
        }
    }

    public void startTimeChallengeSession(View view) {
        if (!validateInputs()) {
            // TODO: Change of Destination
            Intent challengeIntent = new Intent(ProblemSelectionActivity.this, ProblemScreenActivity.class);
            challengeIntent.putExtra("active_user", user);
            challengeIntent.putExtra("play_mode", this.selectedPlayMode);
            challengeIntent.putExtra("play_level", this.selectedPlayLevel);
            startActivity(challengeIntent);
        }
    }

    public void startAssignmentSession(View view) {
        // TODO: Change of Destination
        Intent assignmentIntent = new Intent(ProblemSelectionActivity.this, StudentAssignmentsActivity.class);
        assignmentIntent.putExtra("active_user", user);
        startActivity(assignmentIntent);
    }

    public boolean validateInputs() {
        if (add_switch.isChecked()) {
            selectedPlayMode.add(Operator.valueOf("ADDITION"));
        }
        if (sub_switch.isChecked()) {
            selectedPlayMode.add(Operator.valueOf("SUBTRACTION"));
        }
        if (multi_switch.isChecked()) {
            selectedPlayMode.add(Operator.valueOf("MULTIPLICATION"));
        }
        if (div_switch.isChecked()) {
            selectedPlayMode.add(Operator.valueOf("DIVISION"));
        }
        missingInputAlert = new AlertDialog.Builder(this);
        missingInputAlert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        if (selectedPlayMode.size() == 0 || this.selectedPlayLevel.equals("Select")) {
            AlertDialog alert = missingInputAlert.create();
            alert.setTitle("Error");
            alert.setMessage("Inputs are missing!");
            alert.show();
            return true;
        }
        return false;
    }
}