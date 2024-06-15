package com.example.youtube.addVideo;

import android.app.Activity;
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

import java.io.File;

public class AddVideoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_VIDEO_PICK = 1;
    private EditText etTitle, etDescription, etVideoResId;
    private Button btnAddVideo, btnCancel;
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_CODE_VIDEO_PICK);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            File selectedFile = new File(data.getData().getPath());
            videoPath = selectedFile.getAbsolutePath();
            Toast.makeText(this, "Video Selected: " + videoPath, Toast.LENGTH_SHORT).show();
        }
    }

}