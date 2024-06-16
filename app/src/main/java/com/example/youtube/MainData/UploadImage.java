package com.example.youtube.MainData;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;

import com.example.youtube.RegistrationPage.RegistrationActivity2;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class UploadImage {
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_PHOTO = 2;
    private Activity activity;
    private Uri photoUri; // שמירת ה-URI של התמונה המצולמת

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
                        photoUri = createImageUri(); // יצירת URI לקובץ התמונה
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
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

    private Uri createImageUri() {
        String fileName = "temp_photo_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        return activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedImage = data.getData();
                ((RegistrationActivity2) activity).setProfileImageUri(selectedImage);
            } else if (requestCode == TAKE_PHOTO) {
                ((RegistrationActivity2) activity).setProfileImageUri(photoUri);
            }
        }
    }
}
