package com.example.youtube.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String author;
    private String commentText;

    public Comment(String author, String comment) {
        this.author = author;
        this.commentText = comment;
    }

    public String getAuthor() {
        return author;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String newCommentText) {
        this.commentText =newCommentText;
    }
}
