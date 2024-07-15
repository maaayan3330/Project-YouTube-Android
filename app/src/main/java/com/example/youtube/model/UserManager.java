package com.example.youtube.model;

import androidx.room.Room;

import com.example.youtube.model.daos.UserDao;
import com.example.youtube.utils.MyApplication;
import com.example.youtube.viewModel.UserViewModel;

import java.util.List;

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