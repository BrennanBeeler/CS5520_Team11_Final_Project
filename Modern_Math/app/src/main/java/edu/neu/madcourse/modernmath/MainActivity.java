package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;

public class MainActivity extends AppCompatActivity {
    private User active_user;
    private ArrayList<User> inactive_users = new ArrayList<>();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private UserDatabase local_user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();


        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            ArrayList<User> users = extras.getParcelableArrayList("current_users");
            Log.v("NUM", String.valueOf(users.size()));

            for (int i = 0; i < users.size(); i++)
            {
                if (users.get(i).active)
                {
                    this.active_user = users.get(i);
                }
                else
                {
                    this.inactive_users.add(users.get(i));
                }
            }

            if (this.active_user != null ) {
                // There is an active user

                // TODO: handle active user

                Log.v("HERE_MAIN", this.active_user.toString());

                // Create view of other users is present
                if (this.inactive_users.size() > 0)
                {
                    // TODO: look into this line, as its functionality is unclear
                    if (savedInstanceState == null)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("inactive_users", this.inactive_users);

                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.main_fragment_container, LoginRVFragment.class, bundle)
                                .commit();
                    }
                }
            }
            else
            {
                // If no active user

                // TODO: other users
            }
        }
        else
        {
            // No one has info stored in device


            // TODO: welcome screen
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        UserDao userDao = this.local_user_db.userDao();

        try {
            // Try to get users, but if it takes more than 3 seconds, we assume no active users
            ArrayList<User> currentUsers = executorService.submit(new GetUsers(userDao)).get();

            if (currentUsers != null) {
                for (int i = 0; i < currentUsers.size(); i++)
                {
                    if (currentUsers.get(i).active)
                    {
                        this.active_user = currentUsers.get(i);
                    }
                    else
                    {
                        this.inactive_users.add(currentUsers.get(i));
                    }
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy()
    {
        this.local_user_db.close();
        this.executorService.shutdown();
        super.onDestroy();
    }

    public void addNewUser(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddNewUserActivity.class);

        if (this.active_user != null)
        {
            intent.putExtra("active_user", this.active_user);
        }

        startActivity(intent);
    }

    public void addExistingUser(View view)
    {
        startActivity(new Intent(MainActivity.this, AddExistingUserActivity.class));
    }
}