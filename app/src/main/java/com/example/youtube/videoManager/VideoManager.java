package com.example.youtube.videoManager;

import android.content.Context;

import com.example.youtube.videoDisplay.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VideoManager {

    private final List<Video> videoList;
    private static VideoManager instance;

    public VideoManager() {
        this.videoList = new ArrayList<>();
    }

    public static VideoManager getInstance() {
        if (instance == null) {
            instance = new VideoManager();
        }
        return instance;
    }

    public void addVideo(Video newVideo) {
        videoList.add(newVideo);
    }

    public void addVideos(List<Video> newList) {
        videoList.addAll(newList);
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void loadVideosFromJson(Context context) {
        if (!videoList.isEmpty()) {
            return; // Videos are already loaded, no need to load again
        }
        String json;

        try {
            // Open the JSON file
            InputStream is = context.getAssets().open("videos.json");

            // Get the size of the file
            int size = is.available();

            // Create a buffer to hold the file contents
            byte[] buffer = new byte[size];

            // Read the file into the buffer
            is.read(buffer);

            // Close the input stream
            is.close();

            // Convert the buffer to a String
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace(); // Print the stack trace for debugging
            return ; // Return  if an exception occurs
        }

        // Parse JSON manually
        List<Video> videos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                String videoUri = jsonObject.getString("videoUri");
                String author = jsonObject.getString("author");
                int likes = jsonObject.getInt("likes");
                int views = jsonObject.getInt("views");

                List<Comment> comments = new ArrayList<>();
                if (jsonObject.has("comments")) {
                    JSONArray commentsArray = jsonObject.getJSONArray("comments");
                    for (int j = 0; j < commentsArray.length(); j++) {
                        JSONObject commentObject = commentsArray.getJSONObject(j);
                        String commentAuthor = commentObject.getString("author");
                        String commentText = commentObject.getString("comment");
                        comments.add(new Comment(commentAuthor, commentText));
                    }
                }

                videos.add(new Video(title, description, videoUri, author, likes, views, comments));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.videoList.addAll(videos);
    }
}
