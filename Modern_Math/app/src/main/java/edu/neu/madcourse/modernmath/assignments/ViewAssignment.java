package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;


public class ViewAssignment extends AppCompatActivity {

    // TODO: decide if we need the whole class
    String current_class_id;
    Assignment current_assignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            this.current_class_id = extras.getString("current_class_id");
            this.current_assignment = extras.getParcelable("current_assignment");
        }








    }
}