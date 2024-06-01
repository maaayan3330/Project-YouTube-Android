package com.example.youtube.videoListDisplay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Button btnSignIn = findViewById(R.id.btnSignIn);
        EditText searchBar = findViewById(R.id.etSearchBar);

        // Set up sign-in button click listener
        btnSignIn.setOnClickListener(v -> {
            // Handle sign-in action
            Toast.makeText(this, "Sign In Clicked", Toast.LENGTH_SHORT).show();
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rvListVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
         videoAdapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(videoAdapter);

        // Load initial data
        loadVideos();

    }

    private void loadVideos() {
        // Add sample videos to the list
        videoList.add(new Video("video 1", "Description for video 1", R.raw.video1));
        videoList.add(new Video("video 2", "Description for video 2", R.raw.video1));
        videoList.add(new Video("video 3", "Description for video 3", R.raw.video1));
        videoList.add(new Video("video 4", "Description for video 4", R.raw.video1));
        videoList.add(new Video("video 5", "Description for video 5", R.raw.video1));
        videoList.add(new Video("video 6", "Description for video 6", R.raw.video1));
        videoList.add(new Video("video 7", "Description for video 7", R.raw.video1));
        videoList.add(new Video("video 8", "Description for video 8", R.raw.video1));
        videoList.add(new Video("video 9", "Description for video 9", R.raw.video1));
        videoList.add(new Video("video 10", "Description for video 10", R.raw.video1));

        // Notify the adapter that the data set has changed
        videoAdapter.notifyDataSetChanged();
    }

}

