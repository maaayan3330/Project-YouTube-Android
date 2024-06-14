package com.example.youtube.videoDisplay;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;


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

        // Get data from intent
        Video video = (Video) getIntent().getSerializableExtra("extra_video");

        // Initialize views and Set data to views
        vvVideo = findViewById(R.id.vvVideo);
        TextView titleView = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        titleView.setText(video.getTitle());
        tvDescription.setText(video.getDescription());

        // Get the video resource ID
        int videoResIdInt = getRawResIdByName(video.getVideoResId());
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResIdInt;
        // Set the video path and start playing
        vvVideo.setVideoPath(videoPath);
        vvVideo.start();

        //comment list
        RecyclerView rvCommentsRecyclerView = findViewById(R.id.rvComments);
        List<Comment> commentList = video.getComments() != null ? video.getComments() : new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        rvCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvCommentsRecyclerView.setAdapter(commentAdapter);
        //new comment function
        ImageView iv_post=findViewById(R.id.iv_post);
        EditText et_CommentInput = findViewById(R.id.et_CommentInput);
        iv_post.setOnClickListener(view ->{
            String commentText = et_CommentInput.getText().toString().trim();
            if (!commentText.isEmpty()) {
                Comment newComment = new Comment("User", commentText);
                // Replace "User" with actual user name if available
                commentAdapter.addComment(newComment);
                et_CommentInput.setText("");
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // like function:
        ImageView iv_like = findViewById(R.id.iv_like);
        iv_like.setOnClickListener(view -> {
            view.setSelected(!view.isSelected());
            if (view.isSelected()) {
                // Perform actions when selected
                Toast.makeText(getApplicationContext(), "Liked!", Toast.LENGTH_SHORT).show();
            } else {
                // Perform actions when unselected
                Toast.makeText(getApplicationContext(), "Unliked!", Toast.LENGTH_SHORT).show();
            }
        });
        // share function:
        ImageView iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, video.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this video: " + videoPath);

            startActivity(Intent.createChooser(intent, "Share Video"));
        });
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
        ViewGroup.LayoutParams params = vvVideo.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait mode
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = (int) getResources().getDimension(R.dimen.video_height);
        }
        vvVideo.setLayoutParams(params);
    }
}