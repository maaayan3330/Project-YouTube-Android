package com.example.youtube.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Represents a video with a title, description, resource ID, author, number of likes, and number of views.
 */
@Entity
public class Video implements Serializable {


    @PrimaryKey(autoGenerate=true)
    private int id;
    private String title;       // Title of the video
    private String description; // Description of the video
    private String videoUrl;       // URl of the video
    private String artist;      // Author of the video
    private int likes;          // Number of likes
    private int views;          // Number of views
    private int subscribers;
    private String avatar;
    private Date time;
    private List<Comment> comments;


    public Video(String title, String description, String videoUrl, String artist, int likes, int views, int subscribers, String avatar, Date time, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.artist = artist;
        this.likes = likes;
        this.views = views;
        this.subscribers = subscribers;
        this.avatar = avatar;
        this.time = time;
        this.comments = comments;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
