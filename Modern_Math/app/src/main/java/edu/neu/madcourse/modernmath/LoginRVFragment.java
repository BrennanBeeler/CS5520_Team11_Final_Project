package edu.neu.madcourse.modernmath;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.modernmath.database.User;


public class LoginRVFragment extends Fragment {
    private User active_user;

    public LoginRVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        this.active_user = requireArguments().getParcelable("active_user");

        Log.v("HERE", String.valueOf(this.active_user));

        TextView textView = (TextView) getView().findViewById(R.id.login_fragment_test);
        textView.setText(this.active_user.firstName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_rv, container, false);
    }
}