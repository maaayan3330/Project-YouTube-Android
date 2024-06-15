package com.example.youtube.addVideo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.youtube.R;
import com.example.youtube.videoList.Video;

public class AddVideoActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Uri videoUri;
    private static final int REQUEST_CODE_VIDEO_PICK = 1;

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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_CODE_VIDEO_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            videoUri = data.getData();
            Toast.makeText(this, "Video Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void addVideo() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || videoUri == null) {
            Toast.makeText(this, "Please fill all fields and select a video", Toast.LENGTH_SHORT).show();
            return;
        }

        Video newVideo = new Video(title, description, videoUri.toString(), "Author", 0,0 , null);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_video", newVideo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}