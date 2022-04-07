package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;
import edu.neu.madcourse.modernmath.login.GetUsers;

public class SplashActivity extends AppCompatActivity {

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UserDatabase local_user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Turn on strict mode only if we're debugging
        if (BuildConfig.DEBUG) {
            // The code below is from Android docs on StrictMode.
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    //  .penaltyDeath()  uncomment this to crash if policy is violated instead of just logging
                    .build());
        }

        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();

        UserDao userDao = this.local_user_db.userDao();

        Intent intent = new Intent(this, MainActivity.class);

        Log.v("HERE", "TEST");

        try {
            // Try to get users, but if it takes more than 3 seconds, we assume no active users
            ArrayList<User> currentUsers = executorService.submit(new GetUsers(userDao))
                    .get(3, TimeUnit.SECONDS);

            Log.v("HERE91", String.valueOf(currentUsers.size()));

            if (currentUsers != null) {
                if (currentUsers.size() >= 1)
                {
                    intent.putParcelableArrayListExtra("current_users", currentUsers);
                }
            }
        } catch (ExecutionException e) {
            Log.d("DATABASE", "Clearing local user cache due to execution failure.");

            executorService.submit(userDao::deleteAllUsers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            Log.d("DATABASE", "Clearing local user cache due to timeout.");

            executorService.submit(userDao::deleteAllUsers);
        }

        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy()
    {
        this.local_user_db.close();
        this.executorService.shutdown();
        super.onDestroy();
    }
}