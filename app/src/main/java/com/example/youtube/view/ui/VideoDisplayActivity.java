package com.example.youtube.view.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.model.Comment;
import com.example.youtube.model.User;
import com.example.youtube.model.UserManager;
import com.example.youtube.model.Video;
import com.example.youtube.utils.CustomToast;
import com.example.youtube.view.adapter.CommentAdapter;
import com.example.youtube.viewModel.VideoViewModel;

import java.util.List;

/**
 * Activity to display and play a selected video.
 */
public class VideoDisplayActivity extends AppCompatActivity implements CommentAdapter.CommentAdapterListener {
    private VideoView vvVideo; // VideoView for playing the video
    private ConstraintLayout clControl; // Control buttons layout
    private boolean isFullScreen = false; // Fullscreen indicator
    List<Comment> commentList;

    private Video video;
    private CommentAdapter commentAdapter;
    private VideoViewModel videoViewModel;
    private User currentUser;


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

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        currentUser =UserManager.getInstance().getCurrentUser();

        // Get data from intent
        video = (Video) getIntent().getSerializableExtra("extra_video");

        // Initialize views and set data to views
        vvVideo = findViewById(R.id.vvVideo);
        TextView titleView = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        titleView.setText(video.getTitle());
        tvDescription.setText("Description: " + video.getDescription());
        TextView tv_like = findViewById(R.id.tv_like);
        TextView tv_views = findViewById(R.id.tv_view);
        TextView tv_author = findViewById(R.id.tv_author);
        tv_like.setText("Likes: " + video.getLikes());
        tv_author.setText("Author: " + video.getAuthor());

        // Increment views count when the video starts playing
        video.setViews(video.getViews() + 1);
        videoViewModel.update(video);
        tv_views.setText("Views: " + video.getViews());

        // Set the video URI and start playing
        vvVideo.setVideoURI(Uri.parse(video.getVideoUri()));
        vvVideo.start();

        // Comment list
        RecyclerView rvCommentsRecyclerView = findViewById(R.id.rvComments);
        rvCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, this);
        rvCommentsRecyclerView.setAdapter(commentAdapter);
        commentList=video.getComments();
        commentAdapter.setComments(commentList);


        // New comment function
        ImageView iv_post = findViewById(R.id.iv_post);
        EditText et_CommentInput = findViewById(R.id.et_CommentInput);
        iv_post.setOnClickListener(view -> {
            if (currentUser != null) {
                String commentText = et_CommentInput.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(currentUser.getNickname(), commentText);
                    commentList.add(newComment);
                    commentAdapter.notifyItemInserted(commentList.size() - 1);
                    et_CommentInput.setText("");
                    videoViewModel.update(video);
                } else {
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                CustomToast.showToast(this, "Option available just for register users");
            }
        });

        // Like function:
        ImageView iv_like = findViewById(R.id.iv_like);
        iv_like.setOnClickListener(view -> {
            view.setSelected(!view.isSelected());
            if (view.isSelected()) {
                // Increment likes count and update TextView
                video.setLikes(video.getLikes() + 1);
                tv_like.setText("Likes: " + video.getLikes());
                Toast.makeText(getApplicationContext(), "Liked!", Toast.LENGTH_SHORT).show();

            } else {
                // Decrement likes count and update TextView
                video.setLikes(video.getLikes() - 1);
                tv_like.setText("Likes: " + video.getLikes());
                Toast.makeText(getApplicationContext(), "Unliked!", Toast.LENGTH_SHORT).show();
            }
            videoViewModel.update(video);
        });

        // Share function:
        ImageView iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, video.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this video: " + video.getVideoUri());

            startActivity(Intent.createChooser(intent, "Share Video"));
        });

        // Video control buttons
        clControl = findViewById(R.id.clControl);
        ImageButton btnPlayPause = findViewById(R.id.btnPlayPause);
        ImageButton btnForward = findViewById(R.id.btnForward);
        ImageButton btnRewind = findViewById(R.id.btnRewind);
        ImageButton btnFullscreen = findViewById(R.id.btnFullscreen);

        // Video control function
        btnPlayPause.setOnClickListener(view -> {
            view.setSelected(!view.isSelected());
            if (view.isSelected()) {
                vvVideo.pause();
            } else {
                vvVideo.start();
            }
        });

        btnForward.setOnClickListener(v -> {
            int currentPosition = vvVideo.getCurrentPosition();
            int duration = vvVideo.getDuration();
            if (currentPosition + 10000 < duration) {
                vvVideo.seekTo(currentPosition + 10000);
            } else {
                vvVideo.seekTo(duration);
            }
        });

        btnRewind.setOnClickListener(v -> {
            int currentPosition = vvVideo.getCurrentPosition();
            if (currentPosition - 10000 > 0) {
                vvVideo.seekTo(currentPosition - 10000);
            } else {
                vvVideo.seekTo(0);
            }
        });

        btnFullscreen.setOnClickListener(v -> {
            if (isFullScreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isFullScreen = false;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                isFullScreen = true;
            }
        });

        // Show control buttons when user touches the video
        vvVideo.setOnClickListener(v -> {
            if (clControl.getVisibility() == View.GONE) {
                clControl.setVisibility(View.VISIBLE);
                clControl.postDelayed(() -> clControl.setVisibility(View.GONE), 3000); // Hide after 3 seconds
            } else {
                clControl.setVisibility(View.GONE);
            }
        });

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

    @Override
    public void onEditComment(Comment comment, int position) {
        // Show dialog to edit comment
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Comment");

        final EditText input = new EditText(this);
        input.setText(comment.getCommentText());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newCommentText = input.getText().toString();
            comment.setCommentText(newCommentText);
            videoViewModel.update(video);
            commentAdapter.notifyItemChanged(position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDeleteComment(Comment comment, int position) {
        videoViewModel.update(video);
        commentList.remove(position);
        commentAdapter.notifyItemRemoved(position);
        commentAdapter.notifyItemRangeChanged(position, commentList.size());
    }
}
