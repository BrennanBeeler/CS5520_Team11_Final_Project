package edu.neu.madcourse.modernmath.studentassignments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.CreateAssignmentActivity;
import edu.neu.madcourse.modernmath.assignments.Difficulty;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.assignments.ViewAssignmentActivity;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.leadershipboard.LeadershipScoreComparator;
import edu.neu.madcourse.modernmath.problem_screen.ProblemScreenActivity;
import edu.neu.madcourse.modernmath.teacher.AssignmentListClickListener;
import edu.neu.madcourse.modernmath.teacher.AssignmentListItem;
import edu.neu.madcourse.modernmath.teacher.AssignmentListRVAdapter;
import edu.neu.madcourse.modernmath.teacher.StudentClickListener;
import edu.neu.madcourse.modernmath.teacher.StudentListItem;
import edu.neu.madcourse.modernmath.teacher.StudentListRVAdapter;
import edu.neu.madcourse.modernmath.teacher.TeacherViewClassDetails;
import edu.neu.madcourse.modernmath.teacher.studentassignments.TeacherViewStudentDetailsActivity;

public class StudentAssignmentsActivity extends AppCompatActivity {

    private User active_user;
    private FloatingActionButton addClassCode;
    private String class_code;

    private RecyclerView assignmentListRV;
    private AssignmentListRVAdapter assignmentAdapter;
    private RecyclerView.LayoutManager assignmentLayoutManager;
    final private ArrayList<AssignmentListItem> assignmentList = new ArrayList<>();

    private RecyclerView studentListRV;
    private StudentListRVAdapter studentAdapter;
    private RecyclerView.LayoutManager studentLayoutManager;
    final private ArrayList<StudentListItem> studentList = new ArrayList<>();

    private DatabaseReference myDatabase;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignments_list);
        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        //TextView classCode = findViewById(R.id.class_code_detail_screen);
        //TextView className = findViewById(R.id.class_details_title);
        addClassCode = findViewById(R.id.studentView_floatingActionButton);

        Bundle extras = getIntent().getExtras();

        active_user = extras.getParcelable("active_user");
        class_code = active_user.class_code;
        System.out.println("CLASS CODE " + class_code);
        //classCode.setText(class_code);
        //className.setText(extras.getString("class_title"));


        addClassCode.setOnClickListener(view ->  {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentAssignmentsActivity.this);
                builder.setTitle("Enter your class code!");

                // Set up the input
                final EditText input = new EditText(StudentAssignmentsActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    private DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();;
                    Bundle extras = getIntent().getExtras();
                    User active_user = extras.getParcelable("active_user");
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        class_code = input.getText().toString();

                        Map<String, Object> values = new HashMap<>();
                        values.put("class_code", class_code);
                        this.myDatabase.child("users").child(this.active_user.email).updateChildren(values).addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                            {
                                //finish();
                            }
                            else
                            {
                                Toast.makeText(StudentAssignmentsActivity.this, "There was a problem. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                                Log.v("DB_FAILURE", "Access to db failed when accessing the class code.");
                            }
                        });

                        assignmentRVSetup();
                    }
                });
                builder.show();
        });

        if (class_code != null ) {
            assignmentRVSetup();
        }
    }

    private void assignmentRVSetup() {
        assignmentLayoutManager = new LinearLayoutManager(this);
        assignmentListRV = findViewById(R.id.studentView_recyclerView);
        assignmentListRV.setHasFixedSize(true);
        assignmentAdapter = new AssignmentListRVAdapter(assignmentList);
        assignmentListRV.setAdapter(assignmentAdapter);
        assignmentListRV.setLayoutManager(assignmentLayoutManager);

        // TODO: add placeholder for when no assignments present

        this.myDatabase.child("classes").child(class_code).child("assignments").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assignmentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ArrayList<Operator> operators = new ArrayList<>();
                    if ((Boolean) dataSnapshot.child("addition").getValue())
                    {
                        operators.add(Operator.ADDITION);
                    }
                    if ((Boolean) dataSnapshot.child("subtraction").getValue())
                    {
                        operators.add(Operator.SUBTRACTION);
                    }
                    if ((Boolean) dataSnapshot.child("multiplication").getValue())
                    {
                        operators.add(Operator.MULTIPLICATION);
                    }
                    if ((Boolean) dataSnapshot.child("division").getValue())
                    {
                        operators.add(Operator.DIVISION);
                    }

                    String assignment_id = dataSnapshot.getKey();

                    String title = dataSnapshot.child("assignment_title").getValue().toString();
                    String difficulty = dataSnapshot.child("difficulty").getValue().toString();
                    int num_questions = (int) (long) dataSnapshot.child("num_questions").getValue();
                    int time_limit = (int) (long) dataSnapshot.child("time").getValue();

                    assignmentList.add(new AssignmentListItem(assignment_id, title, operators,
                            difficulty, num_questions, time_limit));
                }
                assignmentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access during assignment access.");
            }
        });

        AssignmentListClickListener i = position -> {
            Intent challengeIntent = new Intent(StudentAssignmentsActivity.this, ProblemScreenActivity.class);
            challengeIntent.putExtra("active_user", active_user);
            challengeIntent.putExtra("play_mode", this.assignmentList.get(position).getOperators());
            challengeIntent.putExtra("play_level", Difficulty.valueOf(this.assignmentList.get(position).getDifficulty()));
            startActivity(challengeIntent);

        };
        assignmentAdapter.setListener(i);
    }

}
