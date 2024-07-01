package com.example.youtube.model;

import androidx.room.Room;

import com.example.youtube.utils.MyApplication;

import java.util.List;

public class UserManager {
    private static UserManager instance;
    private User currentUser;
    private UserDao userDao;

    private UserManager() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "AppDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userDao.insert(user);
    }

    public List<User> getUserList() {
        return userDao.index();
    }

    public User getUserByUsername(String username) {
        List<User> users = userDao.index();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean isExist(String username){
        User user = getUserByUsername(username);
        return user != null;
    }

    public boolean matchAccount(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
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
