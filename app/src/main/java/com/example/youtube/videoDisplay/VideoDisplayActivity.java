package com.example.youtube.videoDisplay;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youtube.R;
import com.example.youtube.videoList.Video;


/**
 * Activity to display and play a selected video.
 */
public class VideoDisplayActivity extends AppCompatActivity {
    private VideoView vvVideo; // VideoView for playing the video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        vvVideo = findViewById(R.id.vvVideo);
        // TextView for displaying the video title
        TextView titleView = findViewById(R.id.tvTitle);
        // TextView for displaying the video description
        TextView tvDescription = findViewById(R.id.tvDescription);

        // Get data from intent
        Video video= (Video) getIntent().getSerializableExtra("extra_video");


        // Set data to views
        titleView.setText(video.getTitle());
        tvDescription.setText(video.getDescription());

        // Get the video resource ID
        int videoResIdInt = getRawResIdByName(video.getVideoResId());
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResIdInt;

        // Set the video path and start playing
        vvVideo.setVideoPath(videoPath);
        vvVideo.start();
    }

    /**
     * Retrieves the resource ID of a raw resource by its name.
     *
     * @param resName The name of the raw resource.
     * @return The resource ID of the raw resource.
     */
    public int getRawResIdByName(String resName) {
        String packageName = getPackageName();
        return getResources().getIdentifier(resName, "raw", packageName);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            ViewGroup.LayoutParams params = vvVideo.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            vvVideo.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait mode
            ViewGroup.LayoutParams params = vvVideo.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = (int) getResources().getDimension(R.dimen.video_height);
            vvVideo.setLayoutParams(params);
        }

    }
}