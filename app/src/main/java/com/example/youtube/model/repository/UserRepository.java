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

    //////////////////////////////////////////bring user in sign in//////////////////////////////////////////////////////
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


    public LiveData<User> getCurrentUser(String username) {
        return getUserByUsernameForCurrent(username);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    public void setCurrentUser(User user) {
        new Thread(() -> {
            currentUserDao.clear();
            currentUserDao.insert(user);

            List<User> currentUsers = currentUserDao.index();
            if (!currentUsers.isEmpty()) {
                Log.d("UserRepository for set", "Current user set to: " + currentUsers.get(0).getUsername());
            } else {
                Log.e("UserRepository for set", "Failed to set current user");
            }
        }).start();
    }

//    public LiveData<User> getCurrentUserToMenu() {
//        MutableLiveData<User> currentUserData = new MutableLiveData<>();
//
//        new Thread(() -> {
//            List<User> users = currentUserDao.index();
//
//            if (users != null && !users.isEmpty()) {
//                Log.d("we rock", "Current user: " + users.get(0).getUsername());
//                currentUserData.postValue(users.get(0));
//            } else {
//                Log.e("we rock", "No current user found");
//                currentUserData.postValue(null);
//            }
//        }).start();
//
//        return currentUserData;
//    }
public void getCurrentUserToMenu(UserCallback callback) {
    new Thread(() -> {
        List<User> users = currentUserDao.index();

        if (users != null && !users.isEmpty()) {
            Log.d("we rock", "Current user: " + users.get(0).getUsername());
            callback.onUserLoaded(users.get(0));
        } else {
            Log.e("we rock", "No current user found");
            callback.onError("No current user found");
        }
    }).start();
}


}
