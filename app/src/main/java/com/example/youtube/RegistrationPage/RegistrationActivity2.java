package com.example.youtube.RegistrationPage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.youtube.R;
import com.example.youtube.SignUpPage.SignUpActivity;
import com.example.youtube.UserManager.UserManager;


public class RegistrationActivity2 extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nicknameEditText;
    private PasswordValidator passwordValidator;
    private UploadImage uploadImage;
    private UserManager userManager;

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

        usernameEditText = findViewById(R.id.TextUserName);
        passwordEditText = findViewById(R.id.TextPassword);
        confirmPasswordEditText = findViewById(R.id.TextVerificationPassword);
        nicknameEditText = findViewById(R.id.TextNikeName);

        // Add button click listener for image upload
        Button uploadImageButton = findViewById(R.id.button_upload_image);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage.showImagePickerDialog();
            }
        });

        // here i connect the button to the next page - if the user is allready register
        Button buttonForSignUp = findViewById(R.id.alredyReg);
        buttonForSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            //start a new activity
            startActivity(intent);
        });


        // Add button click listener for registration
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    // and also add the user to the list of users
                    userManager.addUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                            nicknameEditText.getText().toString());

                    // move to the next page back after the user register good
                    Intent intent = new Intent(RegistrationActivity2.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });
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
