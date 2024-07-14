package com.example.youtube.model.daos;

import com.example.youtube.model.User;

public interface UserCallback {
    void onUserLoaded(User user);
    void onError(String error);
}

