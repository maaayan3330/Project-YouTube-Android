package com.example.youtube.videoDisplay;

import java.io.Serializable;

public class Comment implements Serializable {
    private String author;
    private String comment;

    public Comment(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
