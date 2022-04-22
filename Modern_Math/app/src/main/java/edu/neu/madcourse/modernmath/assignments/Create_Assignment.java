package edu.neu.madcourse.modernmath.assignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.AddNewUserActivity;
import edu.neu.madcourse.modernmath.teacher.AssignmentListItem;

public class Create_Assignment extends AppCompatActivity {

    private Difficulty selected_difficulty;
    private User active_user;
    private String active_class_id;

    private DatabaseReference myDatabase;

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

        if (getIntent().getExtras() != null)
        {
            this.active_user = getIntent().getExtras().getParcelable("active_user");
            this.active_class_id = getIntent().getExtras().getString("active_class_id");
        }

        Log.v("HERE", this.active_user.toString());
        Log.v("HERE1", this.active_class_id);

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        EditText assignment_name_edittext = findViewById(R.id.assignment_name_edittext);

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
            if (assignment_name_edittext.getText().toString().equals(""))
            {
                Toast.makeText(Create_Assignment.this, "Please provide an assignment name",
                        Toast.LENGTH_SHORT).show();
                return;
            }

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
                    Map<String, Object> new_assignment = new HashMap<>();
                    new_assignment.put("assignment_title", assignment_name_edittext.getText().toString());
                    new_assignment.put("difficulty", String.valueOf(selected_difficulty));

                    // Handles case of num questions off so representation is easier
                    if (numQuestionValues[num_questions_picker.getValue()].equals("No target"))
                    {
                        new_assignment.put("num_questions", 0);

                    }
                    else
                    {
                        new_assignment.put("num_questions", Integer.parseInt(numQuestionValues[num_questions_picker.getValue()]));
                    }

                    new_assignment.put("addition", add_switch.isChecked());
                    new_assignment.put("subtraction", sub_switch.isChecked());
                    new_assignment.put("multiplication", multi_switch.isChecked());
                    new_assignment.put("division", div_switch.isChecked());

                    // Handles case of timer off so representation is easier
                    if (timeValues[time_picker.getValue()].equals("Timer off"))
                    {
                        new_assignment.put("time", 0);
                    }
                    else
                    {
                        new_assignment.put("time", Integer.parseInt(timeValues[time_picker.getValue()]));

                    }

                    this.myDatabase.child("classes").child(this.active_class_id).child("assignments").push().setValue(new_assignment)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                            {
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Create_Assignment.this, "There was a problem. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                                Log.v("DB_FAILURE", "Access to db failed when creating new assignment.");
                            }
                        });
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