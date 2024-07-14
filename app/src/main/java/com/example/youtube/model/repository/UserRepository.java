package com.example.youtube.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.api.UserAPI;
import com.example.youtube.model.AppDB;
import com.example.youtube.model.User;
import com.example.youtube.model.daos.UserDao;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private MutableLiveData<User> currentUser;
    private UserAPI userAPI;
    private MutableLiveData<List<User>> userListData;

    public UserRepository(Context context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();
        userListData = new MutableLiveData<>();
        userAPI = new UserAPI(userListData, userDao);
        currentUser = new MutableLiveData<>();

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

    public User getOneUser(String id) {
        List<User> users = userDao.index();
        if (users != null) {
            for (User user : users) {
                if (user.getApiId().equals(id)) {
                    return user;
                }
            }
        }
        return null; // אם המשתמש לא נמצא
    }
    public LiveData<User> getCurrentUser(String username) {
        return getUserByUsernameForCurrent(username);
    }

    private LiveData<User> getUserByUsernameForCurrent(String username) {
        MutableLiveData<User> liveDataUser = new MutableLiveData<>();
        new Thread(() -> {
            List<User> users = userDao.index();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    liveDataUser.postValue(user);
                    return;
                }
            }
            liveDataUser.postValue(null);
        }).start();
        return liveDataUser;
    }


//    public void setCurrentUser(User user) {
//        this.currentUser = new MutableLiveData<user>();
//    }
public void setCurrentUser(User user) {
    currentUser.setValue(user);
    String result = getCurrentUserToMenu().getValue().getUsername();
    Log.d("huu", result);
}

public LiveData<User> getCurrentUserToMenu(){
        return currentUser;
}
}
