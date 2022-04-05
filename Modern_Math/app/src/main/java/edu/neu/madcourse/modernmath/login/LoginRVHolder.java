package edu.neu.madcourse.modernmath.login;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import edu.neu.madcourse.modernmath.R;

public class LoginRVHolder extends RecyclerView.ViewHolder {
    public TextView username;

    public LoginRVHolder(View cardView, final LoginClickListener loginClickListener)
    {
        super(cardView);
        this.username = cardView.findViewById(R.id.user_name);

        cardView.setOnClickListener(view -> {
            if (loginClickListener != null)
            {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    loginClickListener.onLoginClick(position);
                }
            }
        });
    }
}
