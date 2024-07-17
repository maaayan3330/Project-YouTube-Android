package com.example.youtube.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtube.R;
import com.example.youtube.model.User;
import com.example.youtube.model.UserManager;
import com.example.youtube.viewModel.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class EditUserDialogFragment extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editNickname;
    private UserViewModel userViewModel;
    private ImageView profileImage;
    private Uri imageUri;
    private UserManager userManager;
    private String profileImageBase64;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userManager = UserManager.getInstance();

        editNickname = view.findViewById(R.id.edit_nickname);
        profileImage = view.findViewById(R.id.profile_image);
        Button buttonUploadImage = view.findViewById(R.id.button_upload_image);
        Button buttonSave = view.findViewById(R.id.button_save);

        // Set up button listeners
        buttonUploadImage.setOnClickListener(v -> openFileChooser());
        buttonSave.setOnClickListener(v -> saveChanges());

//        // Set current user data
//        User currentUser = userManager.getCurrentUser();
//        if (currentUser != null) {
//            editNickname.setText(currentUser.getNickname());
//            profileImageBase64 = currentUser.getAvatar();
//            setProfileImage(profileImageBase64);
//        }

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageBase64 = convertImageToBase64(imageUri);
            setProfileImage(profileImageBase64);
        }
    }

    private void saveChanges() {
        String newNickname = editNickname.getText().toString().trim();
        File avatarFile = base64ToFile(profileImageBase64);
        userViewModel.updateUser(newNickname, avatarFile);
    }

    private String convertImageToBase64(Uri imageUri) {
        try {
            InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setProfileImage(String base64Image) {
        if (base64Image != null && !base64Image.isEmpty()) {
            if (!base64Image.startsWith("data:image/")) {
                base64Image = "data:image/jpeg;base64," + base64Image;
            }
            byte[] decodedString = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImage.setImageBitmap(decodedByte);
        } else {
            profileImage.setImageResource(R.drawable.profile_pic); // Default image resource
        }
    }

    private File base64ToFile(String base64Image) {
        try {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            File file = new File(getActivity().getCacheDir(), "avatar.jpg");
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(decodedBytes);
                os.flush();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
