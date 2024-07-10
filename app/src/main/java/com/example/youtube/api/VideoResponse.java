package com.example.youtube.api;

import com.example.youtube.model.Video;

import java.util.List;

// the VideoResponse class to match the server response structure
public class VideoResponse {
    private String message;
    private List<Video> videos;

    public String getMessage() {
        return message;
    }
    public List<Video> getVideos() {
        return videos;
    }
}