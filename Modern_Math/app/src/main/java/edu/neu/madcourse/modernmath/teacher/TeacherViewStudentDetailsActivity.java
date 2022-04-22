package edu.neu.madcourse.modernmath.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

public class TeacherViewStudentDetailsActivity extends AppCompatActivity {

    private User active_user;
    private String class_id;
    private String student_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_student_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.active_user = extras.getParcelable("active_user");
            this.class_id = extras.getString("class_id");
            this.student_email = extras.getString("student_email");
        }


    }
}