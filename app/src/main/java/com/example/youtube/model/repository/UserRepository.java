package com.example.youtube.model.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.api.UserAPI;
import com.example.youtube.model.AppDB;
import com.example.youtube.model.User;
import com.example.youtube.model.daos.UserDao;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private User currentUser;
    private UserAPI userAPI;
    private MutableLiveData<List<User>> userListData;

    public UserRepository(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();
        userListData = new MutableLiveData<>();
        userAPI = new UserAPI(userListData, userDao);

        // Load initial data from the server
        userAPI.get();
    }

    public LiveData<List<User>> getAllUsers() {
        return userListData;
    }

    public void insertUser(User user) {
        userAPI.add(user);
    }

    public void updateUser(User user) {
        userAPI.update(user);
    }

    public void deleteUser(User user) {
        userAPI.delete(user);
    }

    public LiveData<Boolean> isExist(String username) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> {
            User user = getUserByUsername(username);
            result.postValue(user != null);
        }).start();
        return result;
    }

    public LiveData<Boolean> matchAccount(String username, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> {
            User user = getUserByUsername(username);
            result.postValue(user != null && user.getPassword().equals(password));
        }).start();
        return result;
    }

    private User getUserByUsername(String username) {
        List<User> users = userDao.index();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public LiveData<User> getCurrentUser() {
        MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();
        currentUserLiveData.postValue(currentUser);
        return currentUserLiveData;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
