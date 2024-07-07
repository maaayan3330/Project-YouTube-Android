package com.example.youtube.view.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youtube.utils.PasswordValidator;
import com.example.youtube.model.User;
import com.example.youtube.utils.UploadImage;
import com.example.youtube.R;
import com.example.youtube.model.UserManager;

public class RegistrationActivity2 extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nicknameEditText;
    private PasswordValidator passwordValidator;
    private UploadImage uploadImage;
    private UserManager userManager;
    // field to keep the photo
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        // Enable edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        passwordValidator = new PasswordValidator();
        uploadImage = new UploadImage(this);
        userManager = UserManager.getInstance(); //  UserManager

        usernameEditText = findViewById(R.id.TextUserName);
        passwordEditText = findViewById(R.id.TextPassword);
        confirmPasswordEditText = findViewById(R.id.TextVerificationPassword);
        nicknameEditText = findViewById(R.id.TextNikeName);

        // Find the logo ImageView
        ImageView logoImage = findViewById(R.id.logoImage);

        // Set up the logo click listener
        logoImage.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity2.this, VideoListActivity.class);
            startActivity(intent);
        });

        // Check and request permissions
        checkPermissions();

        // Add button click listener for image upload
        Button uploadImageButton = findViewById(R.id.button_upload_image);
        uploadImageButton.setOnClickListener(v -> uploadImage.showImagePickerDialog());

        // conect with a buttom if the user is on
        Button buttonForSignUp = findViewById(R.id.alredyReg);
        buttonForSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            // Start a new activity
            startActivity(intent);
        });

        // Add button click listener for registration
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            String resultMessage = passwordValidator.registerUser(
                    RegistrationActivity2.this,
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    confirmPasswordEditText.getText().toString(),
                    nicknameEditText.getText().toString()
            );

            // Display a custom toast message
            showCustomToast(resultMessage);

            // If the user is registered successfully, navigate to the login page
            if (resultMessage.equals("User registered successfully")) {
                // Add the user to the list of users with profile image URI
                User newUser= new User(usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                        nicknameEditText.getText().toString(), profileImageUri.toString());
                userManager.addUser(newUser);

                // Move to the next page back after the user registers successfully
                Intent intent = new Intent(RegistrationActivity2.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // prommiton got
            } else {
                // prommision denided
                Toast.makeText(this, "Camera and storage permissions are required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setProfileImageUri(Uri uri) {
        this.profileImageUri = uri;
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImage.handleActivityResult(requestCode, resultCode, data);
    }
}
