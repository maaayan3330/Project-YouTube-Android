package com.example.youtube.videoList;

import com.example.youtube.videoDisplay.Comment;

import java.io.Serializable;
import java.util.List;
import android.net.Uri;

/**
 * Represents a video with a title, description, resource ID, author, number of likes, and number of views.
 */
public class Video implements Serializable {
    private String title;       // Title of the video
    private String description; // Description of the video
    private Uri videoUri;       // URI of the video
    private String author;      // Author of the video
    private int likes;          // Number of likes
    private int views;          // Number of views
    private List<Comment> comments; // Comments on the video

    /**
     * Constructs a new Video with the specified title, description, URI, author, number of likes, number of views, and comments.
     *
     * @param title       The title of the video.
     * @param description The description of the video.
     * @param videoUri    The URI of the video.
     * @param author      The author of the video.
     * @param likes       The number of likes for the video.
     * @param views       The number of views for the video.
     * @param comments    The comments on the video.
     */
    public Video(String title, String description, Uri videoUri, String author, int likes, int views, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.author = author;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
    }

    // Getters and setters

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

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
