package edu.neu.madcourse.modernmath.login;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;

public class GetUsers implements Callable<ArrayList<User>> {
    UserDao userDao;

    public GetUsers(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public ArrayList<User> call() {
        return new ArrayList<>(userDao.getAll());
    }
}
