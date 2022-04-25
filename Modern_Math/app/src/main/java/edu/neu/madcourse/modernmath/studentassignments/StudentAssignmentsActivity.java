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
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Assignment;
import edu.neu.madcourse.modernmath.assignments.CreateAssignmentActivity;
import edu.neu.madcourse.modernmath.assignments.Difficulty;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.assignments.Student_Assignment;
import edu.neu.madcourse.modernmath.assignments.ViewAssignmentActivity;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.leadershipboard.LeadershipScoreComparator;
import edu.neu.madcourse.modernmath.problem_screen.ProblemScreenActivity;
import edu.neu.madcourse.modernmath.teacher.AssignmentListClickListener;
import edu.neu.madcourse.modernmath.teacher.AssignmentListItem;
import edu.neu.madcourse.modernmath.teacher.AssignmentListRVAdapter;
import edu.neu.madcourse.modernmath.teacher.ClassListItem;
import edu.neu.madcourse.modernmath.teacher.StudentClickListener;
import edu.neu.madcourse.modernmath.teacher.StudentListItem;
import edu.neu.madcourse.modernmath.teacher.StudentListRVAdapter;
import edu.neu.madcourse.modernmath.teacher.TeacherViewClassDetails;
import edu.neu.madcourse.modernmath.teacher.studentassignments.StudentAssignmentCard_AssignmentName;
import edu.neu.madcourse.modernmath.teacher.studentassignments.TeacherViewStudentDetailsActivity;

public class StudentAssignmentsActivity extends AppCompatActivity {

    private User active_user;
    private FloatingActionButton addClassCode;
    private TextView class_code_view;
    private String class_code;

    private RecyclerView assignmentListRV;
    private AssignmentListRVAdapter assignmentAdapter;
    private RecyclerView.LayoutManager assignmentLayoutManager;
    private ArrayList<AssignmentListItem> assignmentList;
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignments_list);
        this.myDatabase = FirebaseDatabase.getInstance().getReference();
        addClassCode = findViewById(R.id.studentView_floatingActionButton);
        class_code_view = findViewById(R.id.class_code_textView);

        Bundle extras = getIntent().getExtras();
        active_user = extras.getParcelable("active_user");
        assignmentList = new ArrayList<>();

        this.myDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(active_user.email)) {

                        class_code = dataSnapshot.getValue(User.class).class_code;
                        if (class_code != null) {
                            assignmentRVSetup();
                        } else {
                            addClassCode.setVisibility(View.VISIBLE);
                            class_code_view.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access ");
            }
        });


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
                    DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
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
                                Task<DataSnapshot> snapshot = this.myDatabase.child("classes").child(class_code).child("assignments").get();
                                snapshot.addOnSuccessListener(result -> {
                                            for (DataSnapshot dataSnapshot : snapshot.getResult().getChildren()) {
                                                Map<String, Object> initial_assignment_details = new HashMap<>();
                                                initial_assignment_details.put("time_spent", "0");
                                                initial_assignment_details.put("num_correct", 0);
                                                initial_assignment_details.put("num_incorrect", 0);

                                                myDatabase.child("classes").child(class_code).child("assignments").child(dataSnapshot.getKey()).child("student_assignments")
                                                        .child(active_user.email).setValue(initial_assignment_details);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(StudentAssignmentsActivity.this, "There was a problem. Please try again.",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.v("DB_FAILURE", "Access to realtime firebase failed when accessing the class code.");
                                        });
                            }
                            else
                            {
                                Toast.makeText(StudentAssignmentsActivity.this, "There was a problem. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                                Log.v("DB_FAILURE", "Access to db failed when accessing the class code.");
                            }
                        });

                        assignmentRVSetup();
                        addClassCode.setVisibility(View.INVISIBLE);
                        class_code_view.setVisibility(View.INVISIBLE);
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

                    boolean completion_status = false;
                    if (dataSnapshot.hasChild("student_assignments")) {
                        if (!dataSnapshot.child("student_assignments").child(active_user.email)
                                .child("time_spent").getValue().toString().equals("0")) {
                            completion_status = true;
                        }
                    }

                    AssignmentListItem item = new AssignmentListItem(assignment_id, title, operators,
                            difficulty, num_questions, time_limit);
                    item.setCompletion_status(completion_status);
                    assignmentList.add(item);
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