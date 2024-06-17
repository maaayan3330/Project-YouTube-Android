package com.example.youtube.videoList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.SignUpActivity;
import com.example.youtube.addVideo.AddVideoActivity;
import com.example.youtube.videoDisplay.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class for displaying a list of videos.
 */
public class VideoListActivity extends AppCompatActivity {
    List<Video> videoList;
    VideoAdapter videoAdapter;
    private static final int REQUEST_CODE_VIDEO_PICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize RecyclerView
        // RecyclerView for displaying the video list
        RecyclerView rvListVideo = findViewById(R.id.rvListVideo);
        rvListVideo.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        // Load videos from JSON
        // List to hold video data
        videoList = loadVideosFromJson();

        // Set adapter to the RecyclerView
        // Adapter for the RecyclerView
        videoAdapter = new VideoAdapter(videoList, this);
        rvListVideo.setAdapter(videoAdapter);

        //sign in function
        ImageView iv_sign_in = findViewById(R.id.ivSignIn);
        iv_sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        //search function
        SearchView sv_search = findViewById(R.id.svSearch);
        sv_search.clearFocus();
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVideoList(newText);
                return true;
            }
        });


        ImageView ivAdd = findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddVideoActivity.class);
            startActivityForResult(intent, REQUEST_CODE_VIDEO_PICK);
        });

    }


    /**
     * Loads video data from the videos.json file in the assets folder.
     *
     * @return A list of Video objects.
     */
    private List<Video> loadVideosFromJson() {
        String json;

        try {
            // Open the JSON file
            InputStream is = getAssets().open("videos.json");

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
            return null; // Return null if an exception occurs
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

        return videos;
    }


    private void filterVideoList(String text) {
        List<Video> filteredList = new ArrayList<>();
        for (Video video : videoList) {
            if (video.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(video);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No video found", Toast.LENGTH_SHORT).show();
        } else {
            videoAdapter.setFilterList(filteredList);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Video newVideo = (Video) data.getSerializableExtra("newVideo");
            if (newVideo != null) {
                videoList.add(newVideo);
                videoAdapter.notifyItemInserted(videoList.size() - 1);
            }
        }
    }
}
