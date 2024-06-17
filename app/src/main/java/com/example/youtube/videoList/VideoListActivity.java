package com.example.youtube.videoList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.SignUpPage.SignUpActivity;
import com.example.youtube.UserManager.User;
import com.example.youtube.addVideo.AddVideoActivity;
import com.example.youtube.design.CustomToast;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.example.youtube.UserManager.UserManager;

import com.google.android.material.imageview.ShapeableImageView;


public class VideoListActivity extends AppCompatActivity {
    List<Video> videoList;
    VideoAdapter videoAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private ShapeableImageView profileImageView;
    private Uri profileImageUri; // Variable to store the profile image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);

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
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.login_yes) {
                    Intent intentForLogIn = new Intent(VideoListActivity.this, SignUpActivity.class);
                    CustomToast.showToast(VideoListActivity.this, "Login");
                    startActivity(intentForLogIn);
                    return true;
                } else if (itemId == R.id.logout_yes) {
                    // Clear user session data
                    UserManager.getInstance().clearCurrentUser();

                    // Navigate to login page
                    Intent intentForLogIn = new Intent(VideoListActivity.this, SignUpActivity.class);
                    CustomToast.showToast(VideoListActivity.this, "Logout");
                    startActivity(intentForLogIn);
                    finish(); // Close the current activity
                    return true;
                } else if (itemId == R.id.upload_data_yes) {
                    Intent intentForVideo = new Intent(VideoListActivity.this, AddVideoActivity.class);
                    CustomToast.showToast(VideoListActivity.this, "Upload video");
                    startActivity(intentForVideo);
                    return true;
                } else if (itemId == R.id.dark_mode_yes) {
                    // Toggle dark mode
                    int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        CustomToast.showToast(VideoListActivity.this, "Switched to Dark Mode");
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        CustomToast.showToast(VideoListActivity.this, "Switched to Light Mode");
                    }
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
        RecyclerView rvListVideo = findViewById(R.id.rvListVideo);
        rvListVideo.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        // Load videos from JSON
        videoList = loadVideosFromJson();

        // Set adapter to the RecyclerView
        videoAdapter = new VideoAdapter(videoList, this);
        rvListVideo.setAdapter(videoAdapter);

        // Initialize user info views from header
        View headerView = navigationView.getHeaderView(0);
        profileImageView = headerView.findViewById(R.id.profileImageView);

        // Load user data from UserManager
        loadUserInfoFromManager();

        // Search function
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

    private void loadUserInfoFromManager() {
        UserManager userManager = UserManager.getInstance();
        User currentUser = userManager.getCurrentUser();

        if (currentUser != null) {
            String username = currentUser.getUsername();
            String nickname = currentUser.getNickname();
            String profileImageUriString = currentUser.getProfileImageUri() != null ? currentUser.getProfileImageUri().toString() : null;

            Log.d("VideoListActivity", "Loading user info: username=" + username + ", nickname=" + nickname);

            if (profileImageUriString != null && !profileImageUriString.isEmpty()) {
                profileImageUri = Uri.parse(profileImageUriString);
                profileImageView.setImageURI(profileImageUri);
            } else {
                profileImageView.setImageResource(R.drawable.profile_pic); // תמונת ברירת מחדל
            }

            // Update Navigation Drawer menu items
            updateNavigationDrawer(username, nickname);
        } else {
            profileImageView.setImageResource(R.drawable.profile_pic);
        }
    }

    private void updateNavigationDrawer(String username, String nickname) {
        MenuItem usernameItem = navigationView.getMenu().findItem(R.id.profile_username);
        MenuItem nicknameItem = navigationView.getMenu().findItem(R.id.profile_nickname);

        if (usernameItem != null) {
            usernameItem.setTitle(username);
        }
        if (nicknameItem != null) {
            nicknameItem.setTitle(nickname);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("VideoListActivity", "onDestroy called");
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
