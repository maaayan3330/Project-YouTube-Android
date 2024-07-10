package com.example.youtube.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Represents a video with a title, description, resource ID, author, number of likes, and number of views.
 */
@Entity
public class Video implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int roomId;
    @SerializedName("_id")
    private String apiId;

    private String userId;
    private String title;       // Title of the video
    private String description; // Description of the video
    private String videoUrl;       // URl of the video
    private String artist;      // Author of the video
    private int likes;          // Number of likes
    private int views;          // Number of views
    private int subscribers;
    private String avatar;
    private String time;
//    private List<String> comments; // List of comment IDs


    public Video(String userId, String title, String description, String videoUrl, String artist, int likes, int views, int subscribers, String avatar, String time) {
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.artist = artist;
        this.likes = likes;
        this.views = views;
        this.subscribers = subscribers;
        this.avatar = avatar;
        this.time = time;
        this.userId = userId;
        this.apiId =new String();
    }

    // Getters and setters

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

}
