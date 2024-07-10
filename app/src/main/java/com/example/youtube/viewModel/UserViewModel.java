package com.example.youtube.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.model.User;
import com.example.youtube.model.repository.UsersRepository;


import java.util.List;

public class UserViewModel extends ViewModel {
    private UsersRepository repository;

    private LiveData<List<User>> users;

    public UserViewModel() {
        repository = new UsersRepository();
        users = repository.getAll();
    }

    public LiveData<List<User>> get() {
        return users;
    }

    public void add(User user) {
        repository.add(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void update(User user) {
        repository.update(user);
    }

}
