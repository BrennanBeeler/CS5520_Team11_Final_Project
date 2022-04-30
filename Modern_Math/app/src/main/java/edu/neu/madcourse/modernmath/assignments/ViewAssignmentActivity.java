package edu.neu.madcourse.modernmath.assignments;

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
import java.util.Optional;
import java.util.StringJoiner;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.teacher.TeacherClassList;


public class ViewAssignmentActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;

    // TODO: decide if we need the whole class
    private String current_class_id;
    private String current_assignment_id;
    private User active_user;

    private Assignment current_assignment;

    private final ArrayList<User> userArrayList = new ArrayList<>();

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
            actionBar.setTitle("View Assignment Details");
            actionBar.setDisplayShowTitleEnabled(true);
            //actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            this.current_class_id = extras.getString("current_class_id");
            this.current_assignment_id = extras.getString("current_assignment_id");
            this.active_user = extras.getParcelable("active_user");
        }

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        // Track users so that the student name can be used instead of username
        this.myDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String username = (dataSnapshot.getKey());
                    if (username != null)
                    {
                        String first_name = (String) dataSnapshot.child("first_name").getValue();
                        String last_name = (String) dataSnapshot.child("last_name").getValue();

                        userArrayList.add(new User(username, first_name, last_name, 0 ,
                                false, false));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access during user retrieval.");
            }
        });

        this.myDatabase.child("classes").child(this.current_class_id).child("assignments")
                .child(this.current_assignment_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentAssignmentList.clear();

                if (snapshot.getKey() != null)
                {
                    // TODO: populate fields for assignment
                    String assignment_name = (String) snapshot.child("assignment_title").getValue();
                    boolean addition = (boolean) snapshot.child("addition").getValue();
                    boolean subtraction = (boolean) snapshot.child("subtraction").getValue();
                    boolean multiplication = (boolean) snapshot.child("multiplication").getValue();
                    boolean division = (boolean) snapshot.child("division").getValue();

                    Difficulty difficulty = Difficulty.valueOf((String) snapshot.child("difficulty").getValue());

                    int num_questions = (int) (long) snapshot.child("num_questions").getValue();

                    int time = (int) (long) snapshot.child("time").getValue();

                    TextView assignment_name_textview = findViewById(R.id.assignment_name);
                    assignment_name_textview.setText(assignment_name);

                    // Convert operators to proper format and set
                    StringJoiner joiner = new StringJoiner(", ");

                    if (addition)
                    {
                        joiner.add(String.valueOf(Operator.ADDITION.value));
                    }
                    if (subtraction)
                    {
                        joiner.add(String.valueOf(Operator.SUBTRACTION.value));
                    }
                    if (multiplication)
                    {
                        joiner.add(String.valueOf(Operator.MULTIPLICATION.value));
                    }
                    if (division)
                    {
                        joiner.add(String.valueOf(Operator.DIVISION.value));
                    }

                    TextView operators_textview = findViewById(R.id.operators);
                    operators_textview.setText("Operators: " + joiner);

                    TextView difficulty_textview = findViewById(R.id.assignment_difficulty);
                    difficulty_textview.setText("Difficulty: " + difficulty);

                    TextView timeLimit_textview = findViewById(R.id.assignment_time_limit);
                    if (time == 0)
                    {
                        timeLimit_textview.setText("Time Limit: Timer off");
                    }
                    else
                    {
                        timeLimit_textview.setText("Time Limit: " + time / 1000 / 60 + " min");
                    }

                    TextView numQuestions_textview = findViewById(R.id.assignment_num_questions);
                    if (num_questions == 0)
                    {
                        numQuestions_textview.setText("Number of questions: No target");
                    }
                    else
                    {
                        numQuestions_textview.setText("Number of questions: " + num_questions);
                    }

                    for (DataSnapshot student_assignment : snapshot.child("student_assignments").getChildren())
                    {
                        String username = student_assignment.getKey();
                        int time_spent = (int) (long) student_assignment.child("time_spent").getValue();
                        int num_correct = (int) (long) student_assignment.child("num_correct").getValue();
                        int num_incorrect = (int) (long) student_assignment.child("num_incorrect").getValue();

                        Optional<User> tempUser = userArrayList.stream().filter(o -> o.username.equals(username)).findFirst();

                        StudentAssignmentCard new_item ;
                        if (tempUser.isPresent())
                        {
                            new_item = new StudentAssignmentCard(username, time_spent,
                                    num_correct, num_incorrect, tempUser.get().firstName + " " + tempUser.get().lastName);
                        }
                        else
                        {
                            new_item = new StudentAssignmentCard(username, time_spent, num_correct, num_incorrect);
                        }
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
                }

                // TODO: will need to check if no students are there/have started assignment
                if (studentAssignmentList.isEmpty())
                {
                    studentAssignmentList.add(new StudentAssignmentCard(null, 0, 0, 0));
                }

                studentAssignmentRVAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access during assignment view.");
            }
        });

        this.recyclerView = findViewById(R.id.student_assignment_recyclerview);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(ViewAssignmentActivity.this);


        this.studentAssignmentRVAdaptor = new StudentAssignmentRVAdaptor(this.studentAssignmentList);
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
                Intent intent = new Intent(ViewAssignmentActivity.this, TeacherClassList.class);
                intent.putExtra("active_user", this.active_user);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}