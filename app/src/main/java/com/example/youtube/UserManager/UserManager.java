package com.example.youtube.UserManager;

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

    public boolean getUserName(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public boolean getUserPassword(String password) {
        for (User user : userList) {
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    // for match password and username
    public boolean matchAccount(String username, String password) {
        for (User user : userList) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}