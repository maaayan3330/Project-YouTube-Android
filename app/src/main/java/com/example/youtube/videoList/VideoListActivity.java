package com.example.youtube.videoList;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * MainActivity class for displaying a list of videos.
 */
public class VideoListActivity extends AppCompatActivity {

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

        // -----------------------------------------------------change button
        // Initialize sign-in button and set its click listener
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> {
            // Handle sign-in action
            Toast.makeText(this, "Sign In Clicked", Toast.LENGTH_SHORT);
        });

        // Initialize RecyclerView
        // RecyclerView for displaying the video list
        RecyclerView rvListVideo = findViewById(R.id.rvListVideo);
        rvListVideo.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        // Load videos from JSON
        // List to hold video data
        List<Video> videoList = loadVideosFromJson();

        // Set adapter to the RecyclerView
        // Adapter for the RecyclerView
        VideoAdapter videoAdapter = new VideoAdapter(videoList, this);
        rvListVideo.setAdapter(videoAdapter);
    }

    /**
     * Loads video data from the videos.json file in the assets folder.
     *
     * @return A list of Video objects.
     */
    private List<Video> loadVideosFromJson() {
        String json = null;

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

        // Parse JSON using Gson
        Gson gson = new Gson();
        Type videoListType = new TypeToken<List<Video>>() {}.getType();

        // Return the list of videos parsed from JSON
        return gson.fromJson(json, videoListType);
    }

    /**
     * Retrieves the resource ID of a raw resource by its name.
     *
     * @param resName The name of the raw resource.
     * @return The resource ID of the raw resource.
     */
    public int getRawResIdByName(String resName) {
        // Get the package name of the application
        String packageName = getPackageName();

        // Get the resource ID
        return getResources().getIdentifier(resName, "raw", packageName);
    }
}
