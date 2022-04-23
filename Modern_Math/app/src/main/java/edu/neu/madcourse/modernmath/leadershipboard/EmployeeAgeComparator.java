package edu.neu.madcourse.modernmath.leadershipboard;

import java.util.Comparator;

import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.login.UserLoginCard;

/**
 * This comparator compares two employees by their ages.
 * @author www.codejava.net
 *
 */
public class EmployeeAgeComparator implements Comparator<User> {

    @Override
    public int compare(User emp1, User emp2) {
        return emp1.age - emp2.age;
    }
}
