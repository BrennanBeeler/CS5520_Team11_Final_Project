package edu.neu.madcourse.modernmath.leadershipboard;

import java.util.Comparator;

import edu.neu.madcourse.modernmath.database.User;

public class LeadershipScoreComparator implements Comparator<User> {

    @Override
    public int compare(User user1, User user2) {
        return user2.answers - user1.answers;
    }
}
