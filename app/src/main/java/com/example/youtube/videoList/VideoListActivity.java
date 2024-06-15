package com.example.youtube.videoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.youtube.R;
import com.example.youtube.SignUpPage.SignUpActivity;
import com.example.youtube.addVideo.AddVideoActivity;
import com.example.youtube.design.CustomToast;
import com.example.youtube.videoList.VideoAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class for displaying a list of videos.
 */
public class VideoListActivity extends AppCompatActivity {
    List<Video> videoList;
    VideoAdapter videoAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Initialize DrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            // that is for the menu pass from evey option to his dest
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.login_yes) {
                    Intent intentForLogIn = new Intent(VideoListActivity.this, SignUpActivity.class);
                    CustomToast.showToast(VideoListActivity.this, "Login");
                    startActivity(intentForLogIn);
                    return true;
                } else if (itemId == R.id.logout_yes) {
                    CustomToast.showToast(VideoListActivity.this, "Logout");
                    return true;
                } else if (itemId == R.id.upload_data_yes) {
                    Intent intentForVideo = new Intent(VideoListActivity.this, AddVideoActivity.class);
                    CustomToast.showToast(VideoListActivity.this, "Upload video");
                    startActivity(intentForVideo);
                    return true;
                } else if (itemId == R.id.dark_mode_yes) {
                    CustomToast.showToast(VideoListActivity.this, "Change Mode");
                    return true;
                } else if (itemId == R.id.Help) {
                    CustomToast.showToast(VideoListActivity.this, "Help");
                    return true;
                } else if (itemId == R.id.Setting) {
                    CustomToast.showToast(VideoListActivity.this, "Setting");
                    return true;
                } else {
                    return false;
                }
            }
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
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
            ex.printStackTrace();
            return null;
        }

        // Parse JSON using Gson
        Gson gson = new Gson();
        Type videoListType = new TypeToken<List<Video>>() {
        }.getType();

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
}