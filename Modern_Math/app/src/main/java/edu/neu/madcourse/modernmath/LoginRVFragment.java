package edu.neu.madcourse.modernmath;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.LoginRVAdaptor;
import edu.neu.madcourse.modernmath.login.UserLoginCard;


public class LoginRVFragment extends Fragment {
    private final ArrayList<UserLoginCard> userList = new ArrayList<>();
    private ArrayList<User> inactive_users;

    private RecyclerView recyclerView;
    private LoginRVAdaptor loginRVAdaptor;
    private RecyclerView.LayoutManager layoutManager;


    public LoginRVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        this.inactive_users = requireArguments().getParcelableArrayList("inactive_users");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_rv, container, false);

        this.recyclerView = view.findViewById(R.id.login_recyclerview);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(view.getContext());

        this.testData();

        this.loginRVAdaptor = new LoginRVAdaptor(this.userList);


//        this.loginRecyclerViewAdaptor.setUsernameClickListener(loginClickListener);
        this.recyclerView.setAdapter(this.loginRVAdaptor);
        this.recyclerView.setLayoutManager(this.layoutManager);

        return view;
    }

    private void testData()
    {
        this.userList.add(new UserLoginCard("Ben"));
        this.userList.add(new UserLoginCard("Jeff"));
        this.userList.add(new UserLoginCard("Jon"));
        this.userList.add(new UserLoginCard("Mike"));
    }
}