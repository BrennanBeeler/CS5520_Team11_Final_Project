package edu.neu.madcourse.modernmath;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.modernmath.database.User;


public class LoginRVFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private User active_user;

    public LoginRVFragment() {
        // Required empty public constructor
    }


//    // TODO: Rename and change types and number of parameters
//    public static LoginRVFragment newInstance() {
//        LoginRVFragment fragment = new LoginRVFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        this.active_user = requireArguments().getParcelable("active_user");
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