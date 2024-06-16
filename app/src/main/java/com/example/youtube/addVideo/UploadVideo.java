package com.example.youtube.addVideo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class UploadVideo {
    public static final int PICK_VIDEO = 1;
    private Activity activity;

    public UploadVideo(Activity activity) {
        this.activity = activity;
    }

    public void showVideoPickerDialog() {
        Intent pickVideo = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickVideo, PICK_VIDEO);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_VIDEO) {
            Uri selectedVideo = data.getData();
            ((AddVideoActivity) activity).setVideoUri(selectedVideo);
        }
    }
}
