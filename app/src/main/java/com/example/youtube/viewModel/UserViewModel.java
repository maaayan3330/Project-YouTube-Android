package com.example.youtube.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.model.User;
import com.example.youtube.model.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = new MutableLiveData<>();
        loadAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public User getOneUser(String id) {
       return userRepository.getOneUser(id);
    }

    private void loadAllUsers() {
        userRepository.getAllUsers().observeForever(users -> allUsers.postValue(users));
    }

    public void insert(User user) {
        userRepository.insertUser(user);
        loadAllUsers();
    }

//    public void update(User user) {
//        userRepository.updateUser(user);
//        loadAllUsers();
//    }
//
//    public void delete(User user) {
//        userRepository.deleteUser(user);
//        loadAllUsers();
//    }

    public LiveData<Boolean> isExist(String username) {
        return userRepository.isExist(username);
    }

    public LiveData<Boolean> matchAccount(String username, String password) {
        return userRepository.matchAccount(username, password);
    }

    public LiveData<User> getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void setCurrentUser(User currentUser) {
        userRepository.setCurrentUser(currentUser);
    }
}

