package edu.neu.madcourse.modernmath.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.CreateAssignmentActivity;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.assignments.ViewAssignmentActivity;
import edu.neu.madcourse.modernmath.database.User;

public class TeacherViewClassDetails extends AppCompatActivity {
    private User teacher;
    private FloatingActionButton addAssignment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_class_details);
        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        TextView classCode = findViewById(R.id.class_code_detail_screen);
        TextView className = findViewById(R.id.class_details_title);
        addAssignment = findViewById(R.id.addAssignment);

        setSupportActionBar(findViewById(R.id.class_list_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(TeacherViewClassDetails.this,
                    "No teacher specified.", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
        teacher = extras.getParcelable("active_user");
        class_code = extras.getString("class_code");
        classCode.setText(class_code);
        className.setText(extras.getString("class_title"));

        addAssignment.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherViewClassDetails.this, CreateAssignmentActivity.class);

            // TODO: null protection?
            intent.putExtra("active_user", this.teacher);
            intent.putExtra("active_class_id", this.class_code);

            startActivity(intent);
        });

        assignmentRVSetup();
        studentRVSetup();
    }

    private void assignmentRVSetup() {
        assignmentLayoutManager = new LinearLayoutManager(this);
        assignmentListRV = findViewById(R.id.teacher_assignments_RV);
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
            Intent intent = new Intent(TeacherViewClassDetails.this, ViewAssignmentActivity.class );
            intent.putExtra("current_class_id", this.class_code);
            intent.putExtra("current_assignment_id", this.assignmentList.get(position).getAssignment_id());
            intent.putExtra("active_user", this.teacher);
            startActivity(intent);
        };
        assignmentAdapter.setListener(i);
    }

    private void studentRVSetup() {
        studentLayoutManager = new LinearLayoutManager(this);
        studentListRV = findViewById(R.id.teacher_students_RV);
        studentListRV.setHasFixedSize(true);
        studentAdapter = new StudentListRVAdapter(studentList);
        studentListRV.setAdapter(studentAdapter);
        studentListRV.setLayoutManager(studentLayoutManager);

        // TODO: add filler for when there are no students

        this.myDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChild("class_code") &&
                            dataSnapshot.child("class_code").getValue().toString().equals(class_code)) {
                        String name = dataSnapshot.child("first_name").getValue().toString()
                                + " " + dataSnapshot.child("last_name").getValue().toString();
                        String email = dataSnapshot.getKey().toString();

                        studentList.add(new StudentListItem(name, email));
                    }
                }

                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access ");
            }
        });

        StudentClickListener i = position -> {
            Intent intent = new Intent(TeacherViewClassDetails.this, TeacherViewStudentDetailsActivity.class );
            intent.putExtra("active_user", teacher);
            intent.putExtra("class_id", class_code);
            intent.putExtra("student_email", studentList.get(position).getEmail());
            startActivity(intent);
        };
        studentAdapter.setListener(i);
    }
}