package edu.neu.madcourse.modernmath.leadershipboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

public class LeadershipActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    private RecyclerView recyclerView;
    private LeadershipAdapter leadershipAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userList = extras.getParcelableArrayList("current_users");
        }

        // To-do: Remove once tested
        User usertest1 = new User("email", "Nidhi", "Shah", 10,true, false);
        User usertest2 = new User("email", "Janan", "Gandhi", 9,true, false);
        User usertest3 = new User("email", "Apo", "Keth", 15,true, false);
        User usertest4 = new User("email", "Spatika", "H", 4,true, false);
        userList.add(usertest1);
        userList.add(usertest2);
        userList.add(usertest3);
        userList.add(usertest4);

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
