package edu.neu.madcourse.modernmath.login;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.database.UserDao;

public class RemoveActiveFromUser implements Runnable {
    UserDao userDao;
    User updated_user;

    public RemoveActiveFromUser(UserDao userDao, User updated_user) {
        this.userDao = userDao;
        this.updated_user = updated_user;
    }

    @Override
    public void run() {

        try {
            userDao.updateUser(this.updated_user);
        } catch (Exception ignored) {

        }
    }
}
