package com.example.youtube.addVideo;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.youtube.R;
import com.example.youtube.videoDisplay.Comment;
import com.example.youtube.videoList.Video;

import java.util.ArrayList;

public class AddVideoActivity extends AppCompatActivity {
    private static final int SELECT_VIDEO = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    private EditText etTitle, etDescription, etVideoResId;
    private String videoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        findViewById(R.id.btnSelectVideo).setOnClickListener(v -> selectVideo());

        findViewById(R.id.btnAddVideo).setOnClickListener(v -> addVideo());

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a video"), SELECT_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VIDEO && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedVideoUri = data.getData();

            if (selectedVideoUri != null) {
                videoPath = selectedVideoUri.toString();
                Log.d("alon12", videoPath);
                Toast.makeText(this, "Video Selected: " + videoPath, Toast.LENGTH_SHORT).show();
                 VideoView vvVideo=findViewById(R.id.vvTest); vvVideo.setVideoURI(selectedVideoUri);vvVideo.start();
            }
        }
    }


    private void addVideo() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        if (title.isEmpty() || description.isEmpty() || videoPath == null) {
            Toast.makeText(this, "Please fill all fields and select a video", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Video object
        Video newVideo = new Video(title, description, videoPath, "Author Name", 0, 0, new ArrayList<>());
///fix hear
        // Create an intent to send the video back to the VideoListActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newVideo", newVideo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}