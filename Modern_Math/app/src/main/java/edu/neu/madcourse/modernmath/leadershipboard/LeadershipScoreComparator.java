package edu.neu.madcourse.modernmath.leadershipboard;

import java.util.Comparator;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.UserLoginCard;

/**
 * This comparator compares two employees by their ages.
 * @author www.codejava.net
 *
 */
public class LeadershipScoreComparator implements Comparator<User> {

    @Override
    public int compare(User user1, User user2) {
        return user1.age - user2.age;
    }
}
