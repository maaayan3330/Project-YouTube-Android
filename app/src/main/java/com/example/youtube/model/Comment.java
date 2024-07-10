package com.example.youtube.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Comment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int roomId;
    @SerializedName("_id")
    private String apiId;

    private String username;
    private String content;
    private String avatar;
    @SerializedName("video")
    private String videoId;
    private String userId;

    public Comment(String videoId, String userId, String username, String content, String avatar) {
        this.videoId = videoId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.avatar = avatar;
        this.apiId = new String();
    }

    // Getters and setters

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String newCommentText) {
        this.content = newCommentText;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

}
