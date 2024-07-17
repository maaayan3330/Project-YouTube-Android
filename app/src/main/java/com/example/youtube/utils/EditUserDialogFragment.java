package com.example.youtube.utils;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class EditUserDialogFragment extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editNickname;
    private ImageView profileImage;
    private Uri imageUri;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        editNickname = view.findViewById(R.id.edit_nickname);
        profileImage = view.findViewById(R.id.profile_image);
        Button buttonUploadImage = view.findViewById(R.id.button_upload_image);
        Button buttonSave = view.findViewById(R.id.button_save);

        buttonUploadImage.setOnClickListener(v -> openFileChooser());
        buttonSave.setOnClickListener(v -> saveChanges());

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
            profileImage.setImageURI(imageUri);
        }
    }

    private void saveChanges() {
        String newNickname = editNickname.getText().toString().trim();
        if (imageUri != null || !newNickname.isEmpty()) {
            User currentUser = UserManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                if (!newNickname.isEmpty()) {
                    currentUser.setNickname(newNickname);
                }
                if (imageUri != null) {
                    currentUser.setAvatar(imageUri.toString());
                }
//                userViewModel.update(currentUser);
                dismiss();
            }
        }
    }
}

