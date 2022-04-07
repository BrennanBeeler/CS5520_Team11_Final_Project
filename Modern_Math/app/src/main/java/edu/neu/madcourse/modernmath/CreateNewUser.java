package edu.neu.madcourse.modernmath;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;

public class CreateNewUser implements Runnable {
    UserDao userDao;
    User newUser;

    public CreateNewUser(UserDao userDao, User newUser) {
        this.userDao = userDao;
        this.newUser = newUser;
    }

    @Override
    public void run() {

        try {
            userDao.insertUser(this.newUser);
        } catch (Exception ignored) {

        }
    }
}
