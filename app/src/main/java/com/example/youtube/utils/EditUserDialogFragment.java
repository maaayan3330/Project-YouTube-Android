package com.example.youtube.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtube.R;
import com.example.youtube.model.User;
import com.example.youtube.model.UserManager;
import com.example.youtube.viewModel.UserViewModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import android.util.Base64;

public class EditUserDialogFragment extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editNickname;
    private UserViewModel userViewModel;
    private Uri imageUri;
    private UserManager userManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userManager = UserManager.getInstance();

        editNickname = view.findViewById(R.id.edit_nickname);
        Button buttonUploadImage = view.findViewById(R.id.button_upload_image);
        Button buttonSave = view.findViewById(R.id.button_save);

        // Set up button listeners
        buttonUploadImage.setOnClickListener(v -> openFileChooser());
        buttonSave.setOnClickListener(v -> saveChanges());

        // Set current user data
        User currentUser = userManager.getCurrentUser();
        if (currentUser != null) {
            editNickname.setText(currentUser.getNickname());
        }

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
        }
    }

    private void saveChanges() {
        String newNickname = editNickname.getText().toString().trim();
        String avatarBase64 = null;
        if (imageUri != null) {
            avatarBase64 = uriToBase64(imageUri);
            Log.d("EditUserDialogFragment", "Avatar file converted to base64");
        } else {
            Log.d("EditUserDialogFragment", "Avatar file is null");
        }
        userViewModel.updateUser(newNickname, avatarBase64);
        dismiss();
    }

    private String uriToBase64(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            Log.e("EditUserDialogFragment", "Failed to convert URI to base64", e);
            return null;
        }
    }
}
