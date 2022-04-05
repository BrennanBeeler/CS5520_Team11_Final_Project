package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.database.UserDatabase;

public class MainActivity extends AppCompatActivity {

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ModernMath);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }
//
//        UserDatabase local_user_db = Room.databaseBuilder(
//                getApplicationContext(), UserDatabase.class, "users-database").build();
//
//        UserDao userDao = local_user_db.userDao();
//
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
////                userDao.insertUser(
////                        new User("test1", "Brennan", "Beeler" , 27, true));
//                List<User> users = userDao.getAll();
//
//                Log.v("HERE", (users.get(0).userID + users.get(0).firstName +
//                        users.get(0).lastName + users.get(0).age + users.get(0).active));
//            }
//        });




        // Retrieve any currently logged in users on this device

        // If there is no logged in user show intro screen

        // If there is a logged in user

            // If a user is marked as last user

                // Go directly to choose math activity screen

                // User can be redirected to select new account from that screen via a button

        // Else

            // show user select


    }


}