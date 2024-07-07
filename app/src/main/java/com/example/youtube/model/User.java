package com.example.youtube.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String username;
    private final String password;
    private final String nickname;
    private final Uri profileImageUri;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
