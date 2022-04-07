package edu.neu.madcourse.modernmath;

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;

public class AddExistingUserActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UserDatabase local_user_db;

    private User prev_active_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing_user);

        if (getIntent().getExtras() != null)
        {
            this.prev_active_user = getIntent().getExtras().getParcelable("active_user");
        }

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();
    }

    public void onProfileSubmit(View view)
    {
        EditText emailEditText = (EditText) findViewById(R.id.email_edittext);
        String email = emailEditText.getText().toString();

        if (email.equals(""))
        {
            Toast.makeText(AddExistingUserActivity.this,
                    "Please enter the email associated with your account!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // To make compiler happy but we know it won't change
        myDatabase.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().getValue() == null)
                    {
                        Toast.makeText(AddExistingUserActivity.this,
                                "There was a problem retrieving your account. " +
                                        "Please make sure your email is entered correctly.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.v("EXISTING", String.valueOf(task.getResult().getValue()));
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
}