package com.example.youtube.videoList;


import com.example.youtube.videoDisplay.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a video with a title, description, and resource ID.
 */
public class Video implements Serializable {
    private String title;       // Title of the video
    private String description; // Description of the video
    private String videoResId;  // Resource ID of the video
    private List<Comment> comments;


    /**
     * Constructs a new Video with the specified title, description, and resource ID.
     *
     * @param title       The title of the video.
     * @param description The description of the video.
     * @param videoResId  The resource ID of the video.
     * @param comments  The comments of the video.
     */
    public Video(String title, String description, String videoResId, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.videoResId = videoResId;
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

    public List<Comment> getComments() {
        return comments;
    }
}
