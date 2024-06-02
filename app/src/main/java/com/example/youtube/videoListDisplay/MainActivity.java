package com.example.youtube.videoListDisplay;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<>();
    private EditText etSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Button btnSignIn = findViewById(R.id.btnSignIn);
        // Set up sign-in button click listener
        btnSignIn.setOnClickListener(v -> {
            // Handle sign-in action
            Toast.makeText(this, "Sign In Clicked", Toast.LENGTH_SHORT).show();
        });


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rvListVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// Load videos from JSON
        videoList = loadVideosFromJson();

        if (videoList != null) {
            videoAdapter = new VideoAdapter(videoList, this);
            recyclerView.setAdapter(videoAdapter);
        } else {
            Log.e(TAG, "Failed to load video list.");
        }


        etSearchBar = findViewById(R.id.etSearchBar);
        // Implement the search functionality


    }

    private List<Video> loadVideosFromJson() {
        String json = null;

        try {
            InputStream is = getAssets().open("videos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            Log.d(TAG, "JSON content: " + json);  // Debugging statement
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


        Gson gson = new Gson();
        Type videoListType = new TypeToken<List<Video>>() {
        }.getType();
        return gson.fromJson(json, videoListType);
    }

    public int getRawResIdByName(String resName) {
        String packageName = getPackageName();
        return getResources().getIdentifier(resName, "raw", packageName);
    }
}