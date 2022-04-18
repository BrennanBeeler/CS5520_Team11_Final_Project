package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;

public class Create_Assignment extends AppCompatActivity {

    Difficulty selected_difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);

        // Set up action bar
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        // Difficulty Selection - Default selection as easy
        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        difficultySpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Difficulty.values()));


        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selected_difficulty = (Difficulty) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        NumberPicker time_picker = findViewById(R.id.time_picker);
        time_picker.setMinValue(0);
        time_picker.setMaxValue(12);

        String[] timeValues = getResources().getStringArray(R.array.time_array);

        time_picker.setDisplayedValues(timeValues);

        NumberPicker num_questions_picker = findViewById(R.id.num_questions_picker);
        num_questions_picker.setMinValue(0);
        num_questions_picker.setMaxValue(20);

        String[] numQuestionValues = getResources().getStringArray(R.array.num_q_array);
        num_questions_picker.setDisplayedValues(numQuestionValues);


        SwitchCompat add_switch = findViewById(R.id.addition_switch);
        SwitchCompat sub_switch = findViewById(R.id.subtraction_switch);
        SwitchCompat multi_switch = findViewById(R.id.multiplication_switch);
        SwitchCompat div_switch = findViewById(R.id.division_switch);

        Button create_button = findViewById(R.id.create_assignment_button);
        create_button.setOnClickListener(view -> {
            if (add_switch.isChecked() || sub_switch.isChecked() ||
                    multi_switch.isChecked() || div_switch.isChecked())
            {
                if (timeValues[time_picker.getValue()].equals("Timer Off")
                        && numQuestionValues[num_questions_picker.getValue()].equals("No target"))
                {
                    Toast.makeText(Create_Assignment.this, "Turn on either the timer " +
                                    "or set a target number of questions to be answered.",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // TODO: create assignment and push to firebase. Will retrieve on previous screen
                    //  Will need to have info on which teacher is making the assignment/what class
                    Toast.makeText(Create_Assignment.this, "TODO- create assignment",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(Create_Assignment.this, "At least one operator " +
                        "must be selected.", Toast.LENGTH_SHORT).show();
            }

        });
    }
}