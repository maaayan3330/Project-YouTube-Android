package com.example.youtube.videoListDisplay;

public class Video {
    private String title;
    private String description;
    private int videoResId;

    // Constructor
    public Video(String title, String description, int videoResId) {
        this.title = title;
        this.description = description;
        this.videoResId = videoResId;
    }

    // Getters and Setters
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

    public int getVideoResId() { return videoResId; }

    public void setVideoResId(int videoResId) {
        this.videoResId = videoResId;
    }
}
