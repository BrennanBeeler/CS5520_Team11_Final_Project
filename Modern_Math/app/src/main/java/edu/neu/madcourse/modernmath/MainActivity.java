package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.login.UserLoginCard;

public class MainActivity extends AppCompatActivity {



    private final ArrayList<UserLoginCard> userList = new ArrayList<>();

    private RecyclerView recyclerView;
    private LoginRVAdaptor loginRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

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


        testData();


//        this.layoutManager = new LinearLayoutManager(this);
//        this.recyclerView = findViewById(R.id.login_recyclerview);
//        this.loginRVAdaptor = new LoginRVAdaptor(this.userList);
//
//
////        this.loginRecyclerViewAdaptor.setUsernameClickListener(loginClickListener);
//        this.recyclerView.setAdapter(this.loginRVAdaptor);
//        this.recyclerView.setLayoutManager(this.layoutManager);

    }

    private void testData()
    {
        this.userList.add(new UserLoginCard("Ben"));
        this.userList.add(new UserLoginCard("Jeff"));
        this.userList.add(new UserLoginCard("Jon"));
        this.userList.add(new UserLoginCard("Mike"));
    }
}