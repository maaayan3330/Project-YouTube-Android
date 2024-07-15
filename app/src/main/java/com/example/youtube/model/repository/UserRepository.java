package com.example.youtube.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.api.UserAPI;
import com.example.youtube.model.AppDB;
import com.example.youtube.model.User;
import com.example.youtube.model.daos.CurrentUserDao;
import com.example.youtube.model.daos.UserCallback;
import com.example.youtube.model.daos.UserDao;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private CurrentUserDao currentUserDao;
    private UserAPI userAPI;
    private MutableLiveData<List<User>> userListData;

    public UserRepository(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();
        currentUserDao = db.currentUserDao();

        userListData = new MutableLiveData<>();
        userAPI = new UserAPI(userListData, userDao);
//        currentUser = new MutableLiveData<>();

        // Load initial data from the server
        userAPI.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return userListData;
    }

    public void insertUser(User user) {
        userAPI.add(user);
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

    public void login(String username, String password) {
        new Thread(() -> {
            currentUserDao.clearCurrentUser();
            currentUserDao.setCurrentUser(username, password);
        }).start();
}

    public LiveData<User> getCurrentUser() {
        MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();
        new Thread(() -> {
            User currentUser = currentUserDao.getCurrentUser();
            currentUserLiveData.postValue(currentUser);
        }).start();
        return currentUserLiveData;
    }

    public void logOut() {
        //call currentUser dao to update the users stored in room
        currentUserDao.clearCurrentUser();
    }
}
