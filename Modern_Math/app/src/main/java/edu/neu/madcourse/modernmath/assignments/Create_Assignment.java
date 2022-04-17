package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

        // Level Selection - Default selection as easy
        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        difficultySpinner.setAdapter(new ArrayAdapter<Difficulty>(this, android.R.layout.simple_spinner_item, Difficulty.values()));


        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selected_difficulty = (Difficulty) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



    }

    public void createNewAssignment(View view)
    {

    }
}