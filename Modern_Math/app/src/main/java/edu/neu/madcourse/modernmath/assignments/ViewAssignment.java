package edu.neu.madcourse.modernmath.assignments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringJoiner;

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

            this.setData();
        }
        else
        {
            this.testMethod();
        }

        // TODO: will need to check if no students are there/have started assignment

        this.recyclerView = findViewById(R.id.student_assignment_recyclerview);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(ViewAssignment.this);


        this.studentAssignmentRVAdaptor = new StudentAssignmentRVAdaptor(this.studentAssignmentList);
        this.recyclerView.setAdapter(this.studentAssignmentRVAdaptor);
        this.recyclerView.setLayoutManager(this.layoutManager);
    }

    private void setData()
    {
        TextView assignment_name = findViewById(R.id.assignment_name);
        assignment_name.setText(this.current_assignment.assignment_title);

        // Convert operators to proper format and set
        TextView operators = findViewById(R.id.operators);
        StringJoiner joiner = new StringJoiner(", ");
        for (Operator op : this.current_assignment.operators)
        {
            joiner.add(String.valueOf(op.value));
        }
        operators.setText("Operators: " + joiner);

        TextView difficulty = findViewById(R.id.assignment_difficulty);
        difficulty.setText("Difficulty: " + this.current_assignment.difficulty);

        TextView timeLimit = findViewById(R.id.assignment_time_limit);
        if (this.current_assignment.time == 0)
        {
            timeLimit.setText("Time Limit: No limit");
        }
        else
        {
            timeLimit.setText("Time Limit: " + this.current_assignment.time);
        }

        TextView numQuestions = findViewById(R.id.assignment_num_questions);
        if (this.current_assignment.num_questions == 0)
        {
            numQuestions.setText("Number of questions: No target");
        }
        else
        {
            numQuestions.setText("Number of questions: " + this.current_assignment.num_questions);
        }

        for (Student_Assignment assignment : this.current_assignment.student_assignments)
        {
            this.studentAssignmentList.add(new StudentAssignmentCard(assignment));
        }
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