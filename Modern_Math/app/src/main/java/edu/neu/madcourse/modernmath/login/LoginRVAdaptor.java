package edu.neu.madcourse.modernmath.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;


public class LoginRVAdaptor extends RecyclerView.Adapter<LoginRVHolder> {

    private final ArrayList<UserLoginCard> userList;
    private LoginClickListener loginClickListener;

    public LoginRVAdaptor(ArrayList<UserLoginCard> userList)
    {
        this.userList = userList;
    }

    public void setUsernameClickListener(LoginClickListener listener)
    {
        this.loginClickListener = listener;
    }

    @NonNull
    @Override
    public LoginRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_login_card, parent, false);
        return new LoginRVHolder(view, this.loginClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginRVHolder viewHolder, int position) {
        viewHolder.username.setText(userList.get(position).getUsername());
    }

    @Override
    public int getItemCount()
    {
        return this.userList.size();
    }
}

