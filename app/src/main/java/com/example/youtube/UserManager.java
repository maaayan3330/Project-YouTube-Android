package com.example.youtube;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> userList;

    public UserManager() {
        userList = new ArrayList<>();
    }

    public void addUser(String username, String password, String nickname) {
        User user = new User(username, password, nickname);
        userList.add(user);
    }

    public List<User> getUserList() {
        return userList;
    }

    public User getUser(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}