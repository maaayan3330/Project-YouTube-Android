package com.example.youtube.UserManager;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> userList;
    private static UserManager instance;
    private User currentUser;

    private UserManager() {
        userList = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(String username, String password, String nickname, Uri profileImageUri) {
        User user = new User(username, password, nickname, profileImageUri);
        userList.add(user);
    }

    public List<User> getUserList() {
        return userList;
    }

    public boolean getUserName(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchAccount(String username, String password) {
        for (User user : userList) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void clearCurrentUser() {
        this.currentUser = null;
    }
}
