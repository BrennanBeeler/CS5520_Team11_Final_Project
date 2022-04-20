package edu.neu.madcourse.modernmath.teacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

public class TeacherViewClassDetails extends AppCompatActivity {
    private User teacher;
    private FloatingActionButton addAssignment;

    private RecyclerView assignmentListRV;
    private AssignmentListRVAdapter assignmentAdapter;
    private RecyclerView.LayoutManager assignmentLayoutManager;
    final private ArrayList<ClassListItem> assignmentList = new ArrayList<>();

    private RecyclerView studentListRV;
    private StudentListRVAdapter studentAdapter;
    private RecyclerView.LayoutManager studentLayoutManager;
    final private ArrayList<ClassListItem> studentList = new ArrayList<>();

    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_class_details);

        TextView teacherName = findViewById(R.id.teacher_name);
        TextView classCode = findViewById(R.id.class_code_detail_screen);
        TextView className = findViewById(R.id.class_details_title);
        addAssignment = findViewById(R.id.addAssignment);

        setSupportActionBar(findViewById(R.id.class_list_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
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
        teacherName.setText(teacher.firstName + " " + teacher.lastName);
        classCode.setText(extras.getString("class_code"));
        className.setText(extras.getString("class_title"));

        addAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: wire into add assignment screen
                Intent intent = new Intent(TeacherViewClassDetails.this, TeacherClassList.class);
                startActivity(intent);
            }
        });


    }


}