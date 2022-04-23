package edu.neu.madcourse.modernmath.leadershipboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

import edu.neu.madcourse.modernmath.MainActivity;
import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;
import edu.neu.madcourse.modernmath.login.LoginClickListener;
import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.login.UserLoginCard;
import edu.neu.madcourse.modernmath.problemselection.ProblemSelectionActivity;
import edu.neu.madcourse.modernmath.teacher.TeacherClassList;

public class LeadershipCollectorActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    private RecyclerView recyclerView;
    private LeadershipAdapter loginRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userList = extras.getParcelableArrayList("current_users");
            System.out.println("SIZE :" + userList.size());
        }

        User usertest1 = new User("email", "Nidhi", "Shah", 10,true, false);
        User usertest2 = new User("email", "JANan", "Gandhi", 9,true, false);
        userList.add(usertest1);
        userList.add(usertest2);

        Collections.sort(userList,
                new EmployeeAgeComparator());

        this.recyclerView = findViewById(R.id.leadershipRecyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);

        this.loginRVAdaptor = new LeadershipAdapter(this.userList);
        this.recyclerView.setAdapter(this.loginRVAdaptor);
        this.recyclerView.setLayoutManager(this.layoutManager);

    }

}
