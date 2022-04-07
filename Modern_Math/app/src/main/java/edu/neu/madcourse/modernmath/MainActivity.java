package edu.neu.madcourse.modernmath;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;
import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.login.UserLoginCard;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<UserLoginCard> userList = new ArrayList<>();


    private User active_user;
    private ArrayList<User> inactive_users = new ArrayList<>();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private UserDatabase local_user_db;

    private RecyclerView recyclerView;
    private LoginRVAdaptor loginRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    private ActivityResultLauncher<Intent> getNewUserIntentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up action bar
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ArrayList<User> users = extras.getParcelableArrayList("current_users");

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).active) {
                    this.active_user = users.get(i);
                } else {
                    this.inactive_users.add(users.get(i));
                }
            }

            this.repopulateList();
        }

        // Set up local db for retrieving updates
        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();

        this.recyclerView = findViewById(R.id.login_recyclerview);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(MainActivity.this);


        this.loginRVAdaptor = new LoginRVAdaptor(this.userList);

//        this.loginRecyclerViewAdaptor.setUsernameClickListener(loginClickListener);
        this.recyclerView.setAdapter(this.loginRVAdaptor);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.getNewUserIntentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK)
            {
                if (result.getData() != null) {
                    // Move previous active user to inactive and replace with new active
                    this.inactive_users.add(0, this.active_user);
                    this.active_user = result.getData().getParcelableExtra("new_user");

                    // Add new active user to top of list
                    this.userList.add(0, new UserLoginCard(this.active_user.firstName
                            + "" + this.active_user.lastName));
                    this.loginRVAdaptor.notifyItemInserted(0);
                    this.recyclerView.scrollToPosition(0);
                }
            }
        });
    }

    private void repopulateList()
    {
        this.userList.clear();

        if (this.active_user != null)
        {
            this.userList.add(new UserLoginCard(this.active_user.firstName
                    + " " + this.active_user.lastName));
        }

        for (User user : this.inactive_users)
        {
            this.userList.add(new UserLoginCard(user.firstName
                    + " " + user.lastName));
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

        this.getNewUserIntentLauncher.launch(intent);
    }

    public void addExistingUser(View view)
    {
        startActivity(new Intent(MainActivity.this, AddExistingUserActivity.class));
    }
}