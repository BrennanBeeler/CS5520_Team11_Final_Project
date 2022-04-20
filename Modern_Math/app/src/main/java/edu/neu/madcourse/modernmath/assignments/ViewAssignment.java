package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.MainActivity;
import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.LoginClickListener;
import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.problemselection.ProblemSelectionActivity;
import edu.neu.madcourse.modernmath.teacher.TeacherClassList;


public class ViewAssignment extends AppCompatActivity {

    // TODO: decide if we need the whole class
    String current_class_id;
    Assignment current_assignment;

    private final ArrayList<StudentAssignmentCard> studentAssignmentList = new ArrayList<>();

    private RecyclerView recyclerView;
    private StudentAssignmentRVAdaptor studentAssignmentRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

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





            for (Student_Assignment assignment : this.current_assignment.student_assignments)
            {
                this.studentAssignmentList.add(new StudentAssignmentCard(assignment));
            }
        }
        else
        {
            this.testMethod();
        }

        this.recyclerView = findViewById(R.id.student_assignment_recyclerview);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(ViewAssignment.this);


        this.studentAssignmentRVAdaptor = new StudentAssignmentRVAdaptor(this.studentAssignmentList);
        this.recyclerView.setAdapter(this.studentAssignmentRVAdaptor);
        this.recyclerView.setLayoutManager(this.layoutManager);
    }

    private void testMethod()
    {
        for (int i = 0; i < 10 ; i++)
        {
            this.studentAssignmentList.add(
                    new StudentAssignmentCard(
                            new Student_Assignment(
                                    "test" + i,
                                    "none",
                                    0,
                                    0)));
        }
    }
}