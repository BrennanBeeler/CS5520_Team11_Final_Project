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
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.teacher.TeacherClassList;

public class TeacherViewStudentDetailsActivity extends AppCompatActivity {

    private User active_user;
    private String class_id;
    private String student_username;
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
            //actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.active_user = extras.getParcelable("active_user");
            this.class_id = extras.getString("class_id");
            this.student_username = extras.getString("student_username");
            this.student_name = extras.getString("student_name");
        }

        TextView student_name_textview = findViewById(R.id.student_name);
        student_name_textview.setText(this.student_name);

        TextView student_username_textview = findViewById(R.id.student_username);
        student_username_textview.setText(this.student_username);

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        this.myDatabase.child("classes").child(this.class_id).child("assignments").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentAssignmentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // TODO: maybe include assignment info in card so teacher can compare easier
                    String assignment_name = (String) dataSnapshot.child("assignment_title").getValue();
                    int time = (int) (long) dataSnapshot.child("time").getValue();
                    int num_questions = (int) (long) dataSnapshot.child("num_questions").getValue();

                    if (dataSnapshot.child("student_assignments").hasChild(student_username))
                    {
                        // The assignment already exists
                        int time_spent = (int) (long) dataSnapshot.child("student_assignments")
                                .child(student_username).child("time_spent").getValue();
                        int num_correct = (int) (long) dataSnapshot.child("student_assignments")
                                .child(student_username).child("num_correct").getValue();
                        int num_incorrect = (int) (long) dataSnapshot.child("student_assignments")
                                .child(student_username).child("num_incorrect").getValue();

                        StudentAssignmentCard_AssignmentName new_item = new StudentAssignmentCard_AssignmentName(
                                assignment_name, time_spent, num_correct, num_incorrect);
                        studentAssignmentList.add(new_item);

                        if (time > 0 && num_questions > 0 ) { // time challenge
                            // successful completion just based on num correct
                            if (num_correct >= num_questions) {
                                new_item.setCompletion_status(true);
                            }
                        } else if (time > 0) { // just practice time
                            if (time_spent >= time) {
                                new_item.setCompletion_status(true);
                            }
                        } else if (num_correct >= num_questions) { // just num_correct
                            new_item.setCompletion_status(true);
                        }
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