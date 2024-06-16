package com.example.youtube.UserManager;

import android.net.Uri;

public class User {
    private String username;
    private String password;
    private String nickname;
    private Uri profileImageUri;

    public User(String username, String password, String nickname, Uri profileImageUri) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUri = profileImageUri;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public Uri getProfileImageUri() {
        return profileImageUri;
    }
}
