package edu.neu.madcourse.modernmath.leadershipboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.UserLoginCard;


public class LeadershipAdapter extends RecyclerView.Adapter<LeadershipHolder> {

    private final ArrayList<User> userList;

    public LeadershipAdapter(ArrayList<User> userList)
    {
        this.userList = userList;
    }

    @NonNull
    @Override
    public LeadershipHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        System.out.println("ADEPTER");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_leadership_user, parent, false);
        return new LeadershipHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadershipHolder viewHolder, int position) {
        viewHolder.username.setText(userList.get(position).firstName);
        viewHolder.user_score.setText(String.valueOf(userList.get(position).age));
        viewHolder.user_rank.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount()
    {
        return this.userList.size();
    }
}


