package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.login.UserLoginCard;

public class MainActivity extends AppCompatActivity {

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    private final ArrayList<UserLoginCard> userList = new ArrayList<>();

    private RecyclerView recyclerView;
    private LoginRVAdaptor loginRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        for (int i = 0; i < 20000; i++)
        {
            Log.v("HERE", String.valueOf(i));
        }

        setTheme(R.style.Theme_ModernMath);
        setContentView(R.layout.activity_main);
//
//        setSupportActionBar(findViewById(R.id.main_toolbar));
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//        {
//            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
//        }
//
//        UserDatabase local_user_db = Room.databaseBuilder(
//                getApplicationContext(), UserDatabase.class, "users-database").build();
//
//        UserDao userDao = local_user_db.userDao();
//
//
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
//
//        testData();
//
//        this.layoutManager = new LinearLayoutManager(this);
//        this.recyclerView = findViewById(R.id.login_recyclerview);
//        this.loginRecyclerViewAdaptor = new LoginRecyclerViewAdaptor(this.userList);
//
////        this.loginRecyclerViewAdaptor.setUsernameClickListener(loginClickListener);
//        this.recyclerView.setAdapter(this.loginRecyclerViewAdaptor);
//        this.recyclerView.setLayoutManager(this.layoutManager);
//
    }

    private void testData()
    {
        this.userList.add(new UserLoginCard("Ben"));
        this.userList.add(new UserLoginCard("Jeff"));
        this.userList.add(new UserLoginCard("Jon"));
        this.userList.add(new UserLoginCard("Mike"));
    }



}