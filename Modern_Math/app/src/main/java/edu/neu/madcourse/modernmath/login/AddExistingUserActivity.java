package edu.neu.madcourse.modernmath.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;

public class AddExistingUserActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UserDatabase local_user_db;

    private User prev_active_user;
    private ArrayList<User> inactive_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing_user);

        if (getIntent().getExtras() != null)
        {
            this.prev_active_user = getIntent().getExtras().getParcelable("active_user");
            this.inactive_users = getIntent().getExtras().getParcelableArrayList("inactive_users");
        }

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("Login");
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();
    }

    public void onProfileSubmit(View view)
    {
        EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
        String username = usernameEditText.getText().toString();

        if (username.equals(""))
        {
            Toast.makeText(AddExistingUserActivity.this,
                    "Please enter the username associated with your account!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (this.prev_active_user != null)
        {
            if (username.equals(this.prev_active_user.username))
            {
                Toast.makeText(AddExistingUserActivity.this, "That account is already " +
                        "logged in on this device.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (this.inactive_users != null && !this.inactive_users.contains(null))
        {
            if (this.inactive_users.stream().map(user -> user.username).anyMatch(user_username -> user_username.equals(username)))
            {
                Toast.makeText(AddExistingUserActivity.this, "That account is already " +
                        "logged in on this device.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (username.contains(".") || username.contains("#") || username.contains("$") ||
                username.contains("[") || username.contains("]"))
        {
            Toast.makeText(AddExistingUserActivity.this,
                    "Usernames cannot contain the following symbols: . # $ [ ]",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Check is user is in firebase db
        myDatabase.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    // If user doesn't exist
                    if (task.getResult().getValue() == null)
                    {
                        Toast.makeText(AddExistingUserActivity.this,
                                "There was a problem retrieving your account. " +
                                        "Please make sure your username is entered correctly.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // If user does exist, save user data to local db and make active
                        String first_name = (String) task.getResult().child("first_name").getValue();
                        String last_name = (String) task.getResult().child("last_name").getValue();
                        int age = (int) (long) task.getResult().child("age").getValue();
                        boolean is_instructor = (boolean) task.getResult().child("instructor").getValue();

                        User existing_user = new User(username, first_name, last_name, age, true,
                                is_instructor);

                        UserDao userDao = local_user_db.userDao();

                        // Set previously active user to not be active
                        if (prev_active_user != null)
                        {
                            prev_active_user.active = false;

                            executorService.submit(new RemoveActiveFromUser(userDao,
                                    prev_active_user));
                        }

                        executorService.submit(new CreateNewUser(userDao, existing_user));

                        // Return data to main about new user
                        Intent data_intent = new Intent();
                        data_intent.putExtra("new_user", existing_user);
                        setResult(Activity.RESULT_OK, data_intent);

                        finish();
                    }
                }
                else
                {
                    Toast.makeText(AddExistingUserActivity.this,
                            "There was a problem retrieving your account. " +
                                    "Please try again.",
                            Toast.LENGTH_SHORT).show();
                    Log.v("DB_FAILURE", "Access to db failed when adding existing account.");
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