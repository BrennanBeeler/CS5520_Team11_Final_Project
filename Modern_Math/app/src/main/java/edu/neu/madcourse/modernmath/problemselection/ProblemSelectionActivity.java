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

import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.AddExistingUserActivity;

/**
 * Class that displays various input options to player to start the game.
 */
public class ProblemSelectionActivity extends AppCompatActivity {

    private String selectedPlayLevel;
    private String[] selectedPlayMode;
    private AlertDialog.Builder missingInputAlert;
    private TextView activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_problem);

        // Welcome Message
        activeUser = findViewById(R.id.activeUserTextView);
        if (getIntent().getExtras() != null)
        {
            User user = getIntent().getExtras().getParcelable("active_user");
            activeUser.setText("Welcome " + user.firstName);
        }


        // Mode Selection - Multiple Selection
        TextView modeTextView = findViewById(R.id.modeSpinner);
        ArrayList<Integer> modeList = new ArrayList<>();
        String[] modeArray = getResources().getStringArray(R.array.mode_array);
        boolean[] selectedPlay = new boolean[modeArray.length];
        selectedPlayMode = new String[modeArray.length];

        modeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProblemSelectionActivity.this);
                builder.setTitle("Select Mode of Play");
                //builder.setCancelable(false);

                builder.setMultiChoiceItems(modeArray, selectedPlay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // if the mode was selected
                        if (b) {
                            modeList.add(i);
                            Collections.sort(modeList);
                        } else {
                            modeList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < modeList.size(); j++) {
                            stringBuilder.append(modeArray[modeList.get(j)]);
                            selectedPlayMode[j] = modeArray[modeList.get(j)];
                            if (j != modeList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        modeTextView.setText(stringBuilder.toString());
                    }
                });
                builder.show();
            }
        });

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
                selectedPlayLevel = (String) parent.getItemAtPosition(position);
                Log.v("level", selectedPlayLevel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }


    public void startPracticeSession(View view) {
        if (!validateInputs()) {
            // To-do: Change of Destination
            Intent practiceIntent = new Intent(ProblemSelectionActivity.this, AddExistingUserActivity.class);
            practiceIntent.putExtra("modeOfPlay", this.selectedPlayMode);
            practiceIntent.putExtra("levelOfPlay", this.selectedPlayLevel);
            startActivity(practiceIntent);
        }
    }

    public void startTimeChallengeSession(View view) {
        if (!validateInputs()) {
            // To-do: Change of Destination
            Intent challengeIntent = new Intent(ProblemSelectionActivity.this, AddExistingUserActivity.class);
            challengeIntent.putExtra("modeOfPlay", this.selectedPlayMode);
            challengeIntent.putExtra("levelOfPlay", this.selectedPlayLevel);
            startActivity(challengeIntent);
        }
    }

    public void startAssignmentSession(View view) {
        // To-do: Change of Destination
        Intent assignmentIntent = new Intent(ProblemSelectionActivity.this, AddExistingUserActivity.class);
        startActivity(assignmentIntent);
    }

    public boolean validateInputs() {
        missingInputAlert = new AlertDialog.Builder(this);
        missingInputAlert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        if (this.selectedPlayMode[0] == null || this.selectedPlayLevel.equals("Select")) {
            AlertDialog alert = missingInputAlert.create();
            alert.setTitle("Error");
            alert.setMessage("Inputs are missing!");
            alert.show();
            return true;
        }
        return false;
    }
}