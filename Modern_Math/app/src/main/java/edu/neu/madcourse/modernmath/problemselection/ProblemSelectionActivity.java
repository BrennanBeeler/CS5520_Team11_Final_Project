package edu.neu.madcourse.modernmath.problemselection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Difficulty;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.studentassignments.StudentAssignmentsActivity;

import edu.neu.madcourse.modernmath.leadershipboard.LeadershipActivity;
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

        // Action Bar
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
            actionBar.setTitle("Problem Selection");
            actionBar.setDisplayShowTitleEnabled(true);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action required
            }
        });

    }


    public void startPracticeSession(View view) {
        if (!validateInputs()) {
            Intent practiceIntent = new Intent(ProblemSelectionActivity.this, ProblemScreenActivity.class);
            practiceIntent.putExtra("active_user", user);
            practiceIntent.putExtra("play_mode", this.selectedPlayMode);
            practiceIntent.putExtra("play_level", this.selectedPlayLevel);
            startActivity(practiceIntent);
        }
    }

    public void startTimeChallengeSession(View view) {
        if (!validateInputs()) {
            Intent challengeIntent = new Intent(ProblemSelectionActivity.this, ProblemScreenActivity.class);
            challengeIntent.putExtra("active_user", user);
            challengeIntent.putExtra("play_mode", this.selectedPlayMode);
            challengeIntent.putExtra("play_level", this.selectedPlayLevel);
            challengeIntent.putExtra("time", 120000);
            startActivity(challengeIntent);
        }
    }

    public void startAssignmentSession(View view) {
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
        if (selectedPlayMode.size() == 0) {
            AlertDialog alert = missingInputAlert.create();
            alert.setTitle("Error");
            alert.setMessage("Please select at least one operator!");
            alert.show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.leader_menu_item, menu);
        menu.findItem(R.id.name).setTitle(user.firstName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leader:
                Intent intent = new Intent(ProblemSelectionActivity.this, LeadershipActivity.class);
                intent.putExtra("active_user", this.user);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}