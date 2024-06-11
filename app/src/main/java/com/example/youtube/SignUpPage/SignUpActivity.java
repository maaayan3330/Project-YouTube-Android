package com.example.youtube.SignUpPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.youtube.UserManager.UserManager;
import com.example.youtube.R;
import com.example.youtube.RegistrationPage.RegistrationActivity2;
import com.example.youtube.videoListDisplay.MainActivity;

public class SignUpActivity extends AppCompatActivity {
    private UserManager userManager;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connect to the right XML
        setContentView(R.layout.activity_sign_up);

        // Initialize UserManager
        userManager = UserManager.getInstance();

        // Find views by ID
        ImageView logoImage = findViewById(R.id.logoImage);
        usernameEditText = findViewById(R.id.TextUserName);
        passwordEditText = findViewById(R.id.TextPassword);

        // Set up the button for sign up
        Button buttonForSignUp = findViewById(R.id.buttonForSignUp);
        buttonForSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity2.class);
            // Start a new activity
            startActivity(intent);
        });

        // Set up the button for login
        Button buttonForHomePage = findViewById(R.id.loginButton);
        buttonForHomePage.setOnClickListener(v -> {
            // Make it string for userManager
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Check if the user exists in the list of users
            boolean answerForUser = userManager.getUserName(username);
            boolean result = userManager.matchAccount(username, password);

            // If the user exists, navigate to the home page
            if (result) {
                Intent intent = new Intent(this, MainActivity.class);
                // Start a new activity
                startActivity(intent);
                showCustomToast("Login successfully!");
            } else {
                if (username.isEmpty() || password.isEmpty()) {
                    showCustomToast("Enter username/password");
                } else if(!answerForUser) {
                    showCustomToast("Username does not exist");
                } else {
                    showCustomToast("Password and username do not match");
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
}
