package edu.neu.madcourse.modernmath.leadershipboard;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

public class LeadershipActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    private RecyclerView recyclerView;
    private LeadershipAdapter leadershipAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_list);
        userList = new ArrayList<>();

        // Action Bar
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
            actionBar.setTitle("Leaderboard");
            actionBar.setDisplayShowTitleEnabled(true);
        }

        this.myDatabase = FirebaseDatabase.getInstance().getReference();
        Task<DataSnapshot> snapshot = this.myDatabase.child("users").get();
        snapshot.addOnSuccessListener(result -> {
            for (DataSnapshot dataSnapshot : snapshot.getResult().getChildren()) {
                if (!dataSnapshot.getValue(User.class).is_teacher) {
                    HashMap<String, Object> user = (HashMap<String, Object>) dataSnapshot.getValue();
                    User u = dataSnapshot.getValue(User.class);
                    u.setFirstName(String.valueOf(user.get("first_name")));
                    u.setLastName(String.valueOf(user.get("last_name")));
                    userList.add(u);

                    Collections.sort(userList,
                            new LeadershipScoreComparator());
                    this.recyclerView = findViewById(R.id.leadershipRecyclerView);
                    this.recyclerView.setHasFixedSize(true);
                    this.layoutManager = new LinearLayoutManager(this);

                    this.leadershipAdapter = new LeadershipAdapter(this.userList);
                    this.recyclerView.setAdapter(this.leadershipAdapter);
                    this.recyclerView.setLayoutManager(this.layoutManager);
                }
            }
            })
                .addOnFailureListener(e -> {

                });

    }

}
