package edu.neu.madcourse.modernmath.teacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

public class TeacherViewClassDetails extends AppCompatActivity {
    private User teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_class_details);

        TextView teacherName = findViewById(R.id.teacher_name);
        TextView classCode = findViewById(R.id.class_code_detail_screen);

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

    }
}