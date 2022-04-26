package edu.neu.madcourse.modernmath.teacher.studentassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.assignments.StudentAssignmentRVAdaptor;
import edu.neu.madcourse.modernmath.assignments.ViewAssignmentActivity;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.teacher.AssignmentListItem;
import edu.neu.madcourse.modernmath.teacher.StudentListItem;
import edu.neu.madcourse.modernmath.teacher.TeacherClassList;

public class TeacherViewStudentDetailsActivity extends AppCompatActivity {

    private User active_user;
    private String class_id;
    private String student_email;
    private String student_name;

    private final ArrayList<StudentAssignmentCard_AssignmentName> studentAssignmentList = new ArrayList<>();

    private RecyclerView recyclerView;
    private StudentAssignment_AssignmentNameRVAdaptor studentAssignmentRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_student_details);

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("View Student Details");
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.active_user = extras.getParcelable("active_user");
            this.class_id = extras.getString("class_id");
            this.student_email = extras.getString("student_email");
            this.student_name = extras.getString("student_name");
        }

        TextView student_name_textview = findViewById(R.id.student_name);
        student_name_textview.setText(this.student_name);

        TextView student_email_textview = findViewById(R.id.student_email);
        student_email_textview.setText(this.student_email);

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        this.myDatabase.child("classes").child(this.class_id).child("assignments").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentAssignmentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // TODO: maybe include assignment info in card so teacher can compare easier
                    String assignment_name = (String) dataSnapshot.child("assignment_title").getValue();

                    if (dataSnapshot.child("student_assignments").hasChild(student_email))
                    {
                        // The assignment already exists
                        int time_spent = (int) (long) dataSnapshot.child("student_assignments")
                                .child(student_email).child("time_spent").getValue();
                        int num_correct = (int) (long) dataSnapshot.child("student_assignments")
                                .child(student_email).child("num_correct").getValue();
                        int num_incorrect = (int) (long) dataSnapshot.child("student_assignments")
                                .child(student_email).child("num_incorrect").getValue();

                        studentAssignmentList.add(new StudentAssignmentCard_AssignmentName(
                                assignment_name, time_spent, num_correct, num_incorrect));
                    }
                    else
                    {
                        // For some reason this student does not yet have an assignment so use default?
                        studentAssignmentList.add(new StudentAssignmentCard_AssignmentName(
                                assignment_name, 0, 0, 0));
                    }
                }
                // placeholder for an empty list
                if (studentAssignmentList.size() == 0) {
                    studentAssignmentList.add(new StudentAssignmentCard_AssignmentName(
                            null, 0, 0, 0));
                }
                studentAssignmentRVAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access during assignment access.");
            }
        });

        // TODO: placeholder for if there are no assignments

        this.recyclerView = findViewById(R.id.student_assignments_rv);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(TeacherViewStudentDetailsActivity.this);


        this.studentAssignmentRVAdaptor = new StudentAssignment_AssignmentNameRVAdaptor(this.studentAssignmentList);
        this.recyclerView.setAdapter(this.studentAssignmentRVAdaptor);
        this.recyclerView.setLayoutManager(this.layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        menu.findItem(R.id.name).setTitle(active_user.firstName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(TeacherViewStudentDetailsActivity.this, TeacherClassList.class);
                intent.putExtra("active_user", this.active_user);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}