package edu.neu.madcourse.modernmath.leadershipboard;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.modernmath.R;

public class LeadershipHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView user_score;
    public Button user_rank;
    public View view;

    public LeadershipHolder(View cardView)
    {
        super(cardView);
        this.username = cardView.findViewById(R.id.leadership_user_name);
        this.user_score = cardView.findViewById(R.id.leadership_user_score);
        this.user_rank = cardView.findViewById(R.id.leadership_button);
        view = cardView;
    }
}
