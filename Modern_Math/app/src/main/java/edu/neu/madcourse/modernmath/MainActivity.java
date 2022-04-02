package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, SplashScreen.class));

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