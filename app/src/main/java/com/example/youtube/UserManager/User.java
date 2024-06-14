package com.example.youtube.UserManager;

import android.net.Uri;

public class User {
    private String username;
    private String password;
    private String nickname;
    private Uri imageUri; // שדה התמונה

    public User(String username, String password, String nickname, Uri imageUri) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.imageUri = imageUri;
    }

    public User(String username, String password, String nickname) {
        this(username, password, nickname, null);
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

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
