package edu.neu.madcourse.modernmath;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.login.UserLoginCard;

public class MainActivity extends AppCompatActivity {



    private final ArrayList<UserLoginCard> userList = new ArrayList<>();

    private RecyclerView recyclerView;
    private LoginRVAdaptor loginRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    private User active_user;
    private ArrayList<User> inactive_users = new ArrayList<>();

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

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            ArrayList<User> users = extras.getParcelableArrayList("current_users");
            Log.v("NUM", String.valueOf(users.size()));

            for (int i = 0; i < users.size(); i++)
            {
                if (users.get(i).active)
                {
                    this.active_user = users.get(i);
                }
                else
                {
                    this.inactive_users.add(users.get(i));
                }
            }

            if (this.active_user != null ) {
                // There is an active user
                // TODO: determine if this is desirable - should stop recreation if isnt needed
                if (savedInstanceState == null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("active_user", this.active_user);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.main_fragment_container, LoginRVFragment.class, bundle)
                            .commit();
                }
            }
            else
            {
                // If no active user
            }
        }
        else
        {
            // No one has info stored in device
            Log.v("HERE", "Extras == null");
        }


//        testData();


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