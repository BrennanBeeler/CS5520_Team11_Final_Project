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

import edu.neu.madcourse.modernmath.MainActivity;
import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.login.AddExistingUserActivity;

public class ProblemSelectionActivity extends AppCompatActivity {

    private String selectedPlayLevel;
    private String[] selectedPlayMode;
    private AlertDialog.Builder missingInputAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_problem);

        TextView textView = findViewById(R.id.modeSpinner);
        ArrayList<Integer> langList = new ArrayList<>();
        String[] langArray = getResources().getStringArray(R.array.mode_array);
        boolean[] selectedPlay = new boolean[langArray.length];
        selectedPlayMode = new String[langArray.length];
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProblemSelectionActivity.this);
                builder.setTitle("Select Mode of Play");
                //builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedPlay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < langList.size(); j++) {
                            stringBuilder.append(langArray[langList.get(j)]);
                            selectedPlayMode[j] = langArray[langList.get(j)];
                            if (j != langList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        textView.setText(stringBuilder.toString());
                    }
                });
                // show dialog
                builder.show();
            }
        });


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
            Intent challengeIntent = new Intent(ProblemSelectionActivity.this, MainActivity.class);
            challengeIntent.putExtra("modeOfPlay", this.selectedPlayMode);
            challengeIntent.putExtra("levelOfPlay", this.selectedPlayLevel);
            startActivity(challengeIntent);
        }
    }

    public boolean validateInputs() {
        missingInputAlert = new AlertDialog.Builder(this);
        missingInputAlert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        if (this.selectedPlayMode[0] == null) {
            AlertDialog alert = missingInputAlert.create();
            alert.setTitle("Error");
            alert.setMessage("Inputs are missing!");
            alert.show();
            return true;
        }
        return false;
    }
}