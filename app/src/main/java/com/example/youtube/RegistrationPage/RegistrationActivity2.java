package com.example.youtube.RegistrationPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Uri selectedImageUri; // משתנה לשמירת ה-URI של התמונה

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
        userManager = UserManager.getInstance(); // קבלת instance של UserManager

        usernameEditText = findViewById(R.id.TextUserName);
        passwordEditText = findViewById(R.id.TextPassword);
        confirmPasswordEditText = findViewById(R.id.TextVerificationPassword);
        nicknameEditText = findViewById(R.id.TextNikeName);

        // Add button click listener for image upload
        Button uploadImageButton = findViewById(R.id.button_upload_image);
        uploadImageButton.setOnClickListener(v -> uploadImage.showImagePickerDialog());

        // כאן אני מקשר את הכפתור לדף ההתחברות - אם המשתמש כבר רשום
        Button buttonForSignUp = findViewById(R.id.alredyReg);
        buttonForSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
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
                // הוספת המשתמש לרשימת המשתמשים עם התמונה שנבחרה
                userManager.addUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                        nicknameEditText.getText().toString(), selectedImageUri);

                // מעבר לדף הבא לאחר הרשמה מוצלחת
                Intent intent = new Intent(RegistrationActivity2.this, SignUpActivity.class);
                startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UploadImage.PICK_IMAGE || requestCode == UploadImage.TAKE_PHOTO) {
                selectedImageUri = data.getData();
                // עדכון התמונה שנבחרה או צולמה
            }
        }
    }
}
