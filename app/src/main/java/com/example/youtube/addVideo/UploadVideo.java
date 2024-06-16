package com.example.youtube.addVideo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import android.content.ContentValues;

import com.example.youtube.addVideo.AddVideoActivity;

public class UploadVideo {
    public static final int PICK_VIDEO = 1;
    public static final int TAKE_VIDEO = 2;
    private Activity activity;
    private Uri videoUri;

    public UploadVideo(Activity activity) {
        this.activity = activity;
    }

    public void showVideoPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Video");
        builder.setItems(new CharSequence[]{"Take Video", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Record video from camera
                        Intent takeVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoUri = createVideoUri();
                        takeVideo.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        activity.startActivityForResult(takeVideo, TAKE_VIDEO);
                        break;
                    case 1:
                        // Choose from gallery
                        Intent pickVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(pickVideo, PICK_VIDEO);
                        break;
                }
            }
        });
        builder.show();
    }

    private Uri createVideoUri() {
        String fileName = "temp_video_" + System.currentTimeMillis() + ".mp4";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, fileName);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        return activity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_VIDEO) {
                Uri selectedVideo = data.getData();
                ((AddVideoActivity) activity).setVideoUri(selectedVideo);
            } else if (requestCode == TAKE_VIDEO) {
                ((AddVideoActivity) activity).setVideoUri(videoUri);
            }
        }
    }
}
