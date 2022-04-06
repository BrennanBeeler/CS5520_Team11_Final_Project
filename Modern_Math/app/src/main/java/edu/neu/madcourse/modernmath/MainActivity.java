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

                // TODO: handle active user

                Log.v("ActiveUser", this.active_user.toString());

                // Create view of other users is present
                if (this.inactive_users.size() > 0)
                {
                    // TODO: look into this line, as its functionality is unclear
                    if (savedInstanceState == null)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("inactive_users", this.inactive_users);

                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.main_fragment_container, LoginRVFragment.class, bundle)
                                .commit();
                    }
                }
            }
            else
            {
                // If no active user

                // TODO: other users
            }
        }
        else
        {
            // No one has info stored in device


            // TODO: welcome screen
        }




    }

}