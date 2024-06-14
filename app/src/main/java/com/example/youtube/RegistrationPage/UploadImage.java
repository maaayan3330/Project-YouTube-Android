package com.example.youtube.RegistrationPage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;

public class UploadImage {
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;
    private Activity activity;

    public UploadImage(Activity activity) {
        this.activity = activity;
    }

    public void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Image");
        builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Take photo from camera
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(takePicture, TAKE_PHOTO);
                        break;
                    case 1:
                        // Choose from gallery
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(pickPhoto, PICK_IMAGE);
                        break;
                }
            }
        });
        builder.show();
    }
}
