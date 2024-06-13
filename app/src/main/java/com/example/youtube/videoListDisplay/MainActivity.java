package com.example.youtube.videoListDisplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.youtube.R;
import com.example.youtube.SignUpPage.SignUpActivity;
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

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvListVideo;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.activity_main);

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
                    Intent intentForLogIn = new Intent(MainActivity.this, SignUpActivity.class);
                    CustomToast.showToast(MainActivity.this, "Login");
                    startActivity(intentForLogIn);
                    return true;
                } else if (itemId == R.id.logout_yes) {
                    CustomToast.showToast(MainActivity.this, "Logout");
                    return true;
                } else if (itemId == R.id.upload_data_yes) {
                    Intent intentForVideo = new Intent(MainActivity.this, AddVideoActivity.class);
                    CustomToast.showToast(MainActivity.this, "Upload video");
                    startActivity(intentForVideo);
                    return true;
                } else if (itemId == R.id.dark_mode_yes) {
                    CustomToast.showToast(MainActivity.this, "Change Mode");
                    return true;
                } else if (itemId == R.id.Help) {
                    CustomToast.showToast(MainActivity.this, "Help");
                    return true;
                } else if (itemId == R.id.Setting) {
                    CustomToast.showToast(MainActivity.this, "Setting");
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Initialize RecyclerView
        rvListVideo = findViewById(R.id.rvListVideo);
        rvListVideo.setLayoutManager(new LinearLayoutManager(this));

        // Load videos from JSON
        videoList = loadVideosFromJson();

        // Set adapter to the RecyclerView
        videoAdapter = new VideoAdapter(videoList, this);
        rvListVideo.setAdapter(videoAdapter);
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
        String json = null;

        try {
            InputStream is = getAssets().open("videos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        Gson gson = new Gson();
        Type videoListType = new TypeToken<List<Video>>() {}.getType();
        return gson.fromJson(json, videoListType);
    }

    public int getRawResIdByName(String resName) {
        String packageName = getPackageName();
        return getResources().getIdentifier(resName, "raw", packageName);
    }
}
