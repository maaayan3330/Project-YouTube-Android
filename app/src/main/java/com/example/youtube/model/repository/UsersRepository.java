package com.example.youtube.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.model.AppDB;
import com.example.youtube.model.User;
import com.example.youtube.model.daos.UserDao;
import com.example.youtube.utils.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    private UserDao userDao;
    private UsersRepository.UserListData userListData;

    public UsersRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "UsersDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao();
        userListData = new UserListData();

    }

    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new LinkedList<User>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                userListData.postValue(userDao.index());
            }).start();
        }
    }

    public LiveData<List<User>> getAll() {
        return userListData;
    }

    public void add(User user) {
        new Thread(() -> {
            userDao.insert(user);
        }).start();
    }

    public void delete(User user) {
        new Thread(() -> {
            userDao.delete(user);
        }).start();
    }

    public void update(User user) {
        new Thread(() -> {
            userDao.update(user);
        }).start();
    }

}
