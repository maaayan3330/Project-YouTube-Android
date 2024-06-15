package com.example.youtube.UserManager;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> userList;
    private static UserManager instance;

    private UserManager() {
        userList = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(String username, String password, String nickname) {
        User user = new User(username, password, nickname);
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
}