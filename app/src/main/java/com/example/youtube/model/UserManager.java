package com.example.youtube.model;


import java.util.List;
import java.util.ArrayList;

public class UserManager {
//    private final List<User> userList;
    private static UserManager instance;
    private User currentUser;

//    private UserManager() {
//        userList = new ArrayList<>();
//    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
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