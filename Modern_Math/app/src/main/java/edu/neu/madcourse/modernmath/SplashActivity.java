package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;

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

        // Remove once working
        for (int i = 0; i < 20000; i++)
        {
            Log.v("HERE", String.valueOf(i));
        }

        this.local_user_db = Room.databaseBuilder(
                getApplicationContext(), UserDatabase.class, "users-database").build();

        UserDao userDao = this.local_user_db.userDao();

        try {
            User currentUser = executorService.submit(new DatabaseQuery(userDao)).get();
            if (currentUser != null)
            {
                Log.v("TEST1", (currentUser.userID + currentUser.firstName +
                        currentUser.lastName + currentUser.age + currentUser.active));
            }
            else
            {
                Log.v("TEST1", "is null");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onDestroy()
    {
        this.local_user_db.close();
        super.onDestroy();
    }

    public class DatabaseQuery implements Callable<User>
    {
        UserDao userDao;

        public DatabaseQuery(UserDao userDao)
        {
            this.userDao = userDao;
        }

        @Override
        public User call()
        {

            try {
                userDao.insertUser(new User("test1", "Brennan",
                        "Beeler" , 27, true));
            }
            catch (Exception ignored)
            {

            }

            List<User> users = userDao.getActiveUser();
            Log.v("TEST2", String.valueOf(users));


            if (users.size() < 1)
            {
                return null;
            }
            else
            {
                return users.get(0);
            }
        }
    }
}