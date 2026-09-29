package data;

import business.User;
import java.util.ArrayList;
import java.util.List;

public class UserDB {
    private static final List<User> users = new ArrayList<>();

    public static synchronized boolean emailExists(String email) {
        return getUser(email) != null;
    }

    public static synchronized User getUser(String email) {
        for (User u : users) {
            if (u.getEmail().trim().equalsIgnoreCase(email.trim())) {
                return u;
            }
        }
        return null;
    }

    public static synchronized void insert(User user) {
        users.add(user);
    }
}