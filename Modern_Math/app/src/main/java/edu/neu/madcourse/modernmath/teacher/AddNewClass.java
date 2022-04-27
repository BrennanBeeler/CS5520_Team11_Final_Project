package edu.neu.madcourse.modernmath.teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.login.AddNewUserActivity;
import edu.neu.madcourse.modernmath.login.CreateNewUser;
import edu.neu.madcourse.modernmath.login.RemoveActiveFromUser;

public class AddNewClass extends AppCompatActivity {
    private User teacher;
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_class);

        setSupportActionBar(findViewById(R.id.add_class_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("Add New Class");
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();
        teacher = extras.getParcelable("active_user");
        this.myDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onNewClassSubmit(View view) {
        EditText classTitleEditText = (EditText) findViewById(R.id.class_title);
        EditText periodEditText = (EditText) findViewById(R.id.new_class_period);
        EditText joinCodeEditText = (EditText) findViewById(R.id.new_class_join_code);

        String classTitle = classTitleEditText.getText().toString();
        String period = periodEditText.getText().toString();
        String joinCode = joinCodeEditText.getText().toString();

        if (classTitle.equals("") || period.equals("") || joinCode.equals(""))
        {
            Toast.makeText(AddNewClass.this,
                    "Please make sure to complete all fields!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Map<String, Object> newClass = new HashMap<>();
        newClass.put("class_title", classTitle);
        newClass.put("class_period", period);
        newClass.put("teacher_email", teacher.email);

        myDatabase.child("classes").child(joinCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    // If unsuccessful in retrieving this, the email is available
                    if (task.getResult().getValue() == null) {
                        myDatabase.child("classes").child(joinCode).setValue(newClass, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    // We have successfully created the account
                                    // Return data to main about teacher
                                    Intent data_intent = new Intent();
                                    setResult(Activity.RESULT_OK, data_intent);
                                    data_intent.putExtra("active_user", teacher);
                                    finish();
                                }
                                else {
                                    Toast.makeText(AddNewClass.this,
                                            "There was a problem creating your class. Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                    else {
                        Toast.makeText(AddNewClass.this,
                                "That Join Code is already in use!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                else {
                    Toast.makeText(AddNewClass.this,
                            "There was a problem creating your class. Please try again.",
                            Toast.LENGTH_SHORT).show();
                    Log.v("DB_FAILURE", "Access to db failed when creating new user.");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        menu.findItem(R.id.name).setTitle(teacher.firstName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(AddNewClass.this, TeacherClassList.class);
                intent.putExtra("active_user", this.teacher);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}