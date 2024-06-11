package com.example.youtube.videoDisplay;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.videoList.Video;

import java.util.List;


/**
 * Activity to display and play a selected video.
 */
public class VideoDisplayActivity extends AppCompatActivity {
    private VideoView vvVideo; // VideoView for playing the video
    private RecyclerView rvCommentsRecyclerView;


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
        TextView titleView = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        rvCommentsRecyclerView = findViewById(R.id.rvComments);
        rvCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get data from intent
        Video video = (Video) getIntent().getSerializableExtra("extra_video");


        // Set data to views
        titleView.setText(video.getTitle());
        tvDescription.setText(video.getDescription());

        // Get the video resource ID
        int videoResIdInt = getRawResIdByName(video.getVideoResId());
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResIdInt;

        // Set the video path and start playing
        vvVideo.setVideoPath(videoPath);
        vvVideo.start();

        ImageView likeButton = findViewById(R.id.iv_like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    // Perform actions when selected
                    Toast.makeText(getApplicationContext(), "Liked!", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform actions when unselected
                    Toast.makeText(getApplicationContext(), "Unliked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        List<Comment> commentList = video.getComments();
        if (commentList != null) {
            CommentAdapter commentAdapter = new CommentAdapter(commentList);
            rvCommentsRecyclerView.setAdapter(commentAdapter);
        }
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