package com.example.youtube.videoList;

import com.example.youtube.videoDisplay.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a video with a title, description, resource ID, author, number of likes, and number of views.
 */
public class Video implements Serializable {
    private String title;       // Title of the video
    private String description; // Description of the video
    private String videoResId;  // Resource ID of the video
    private String author;      // Author of the video
    private int likes;          // Number of likes
    private int views;          // Number of views
    private List<Comment> comments; // Comments on the video

    /**
     * Constructs a new Video with the specified title, description, resource ID, author, number of likes, number of views, and comments.
     *
     * @param title       The title of the video.
     * @param description The description of the video.
     * @param videoResId  The resource ID of the video.
     * @param author      The author of the video.
     * @param likes       The number of likes for the video.
     * @param views       The number of views for the video.
     * @param comments    The comments on the video.
     */
    public Video(String title, String description, String videoResId, String author, int likes, int views, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.videoResId = videoResId;
        this.author = author;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
    }

    /**
     * Returns the title of the video.
     *
     * @return The title of the video.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the video.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the video.
     *
     * @return The description of the video.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the video.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the resource ID of the video.
     *
     * @return The resource ID of the video.
     */
    public String getVideoResId() {
        return videoResId;
    }

    /**
     * Sets the resource ID of the video.
     *
     * @param videoResId The resource ID to set.
     */
    public void setVideoResId(String videoResId) {
        this.videoResId = videoResId;
    }

    /**
     * Returns the author of the video.
     *
     * @return The author of the video.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the video.
     *
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the number of likes for the video.
     *
     * @return The number of likes for the video.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Sets the number of likes for the video.
     *
     * @param likes The number of likes to set.
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Returns the number of views for the video.
     *
     * @return The number of views for the video.
     */
    public int getViews() {
        return views;
    }

    /**
     * Sets the number of views for the video.
     *
     * @param views The number of views to set.
     */
    public void setViews(int views) {
        this.views = views;
    }

    /**
     * Returns the comments on the video.
     *
     * @return The comments on the video.
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Sets the comments on the video.
     *
     * @param comments The comments to set.
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
