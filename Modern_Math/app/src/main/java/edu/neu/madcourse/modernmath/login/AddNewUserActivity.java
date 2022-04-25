package edu.neu.madcourse.modernmath.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;

public class AddNewUserActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UserDatabase local_user_db;

    private User prev_active_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        if (getIntent().getExtras() != null)
        {
            this.prev_active_user = getIntent().getExtras().getParcelable("active_user");
        }

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("Add New User");
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();
    }

    public void onProfileSubmit(View view)
    {
        EditText emailEditText = (EditText) findViewById(R.id.email_edittext);
        EditText firstNameEditText = (EditText) findViewById(R.id.first_name_edittext);
        EditText lastNameEditText = (EditText) findViewById(R.id.last_name_edittext);
        EditText ageEditText = (EditText) findViewById(R.id.age_edittext);
        CheckBox instructor_checkbox = (CheckBox) findViewById(R.id.instructor_checkbox);

        String email = emailEditText.getText().toString();
        String first_name = firstNameEditText.getText().toString();
        String last_name = lastNameEditText.getText().toString();
        boolean instructor_checked = instructor_checkbox.isChecked();

        if (email.equals("") || first_name.equals("") || last_name.equals(""))
        {
            Toast.makeText(AddNewUserActivity.this,
                    "Please make sure to complete all fields!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        int age = 0;
        try {
            age = Integer.parseInt(ageEditText.getText().toString());
        }
        catch (Exception NumberFormatException)
        {
            Toast.makeText(AddNewUserActivity.this,
                    "Please enter your age so we can better customize your profile!",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("first_name", first_name);
        newUser.put("last_name", last_name);
        newUser.put("age", age);
        newUser.put("instructor", instructor_checked);

        User new_user = new User(email, first_name, last_name, age, true, instructor_checked);

        myDatabase.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    // If unsuccessful in retrieving this, the email is available
                    if (task.getResult().getValue() == null)
                    {
                        myDatabase.child("users").child(email).setValue(newUser, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null)
                                {
                                    // We have successfully created the account
                                    UserDao userDao = local_user_db.userDao();

                                    // Set previously active user to not be active
                                    if (prev_active_user != null)
                                    {
                                        prev_active_user.active = false;

                                        executorService.submit(new RemoveActiveFromUser(userDao,
                                                prev_active_user));
                                    }

                                    executorService.submit(new CreateNewUser(userDao, new_user));

                                    // Return data to main about new user
                                    Intent data_intent = new Intent();
                                    data_intent.putExtra("new_user", new_user);
                                    setResult(Activity.RESULT_OK, data_intent);

                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(AddNewUserActivity.this,
                                            "There was a problem creating your account. Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(AddNewUserActivity.this,
                                "That email is already in use!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                else
                {
                    Toast.makeText(AddNewUserActivity.this,
                            "There was a problem creating your account. Please try again.",
                            Toast.LENGTH_SHORT).show();
                    Log.v("DB_FAILURE", "Access to db failed when creating new user.");
                }
            }
        });
    }

    @Override
    public void onDestroy()
    {
        this.local_user_db.close();
        this.executorService.shutdown();
        super.onDestroy();
    }
}