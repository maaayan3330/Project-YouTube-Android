package com.example.youtube.SignUpPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.youtube.UserManager.User;
import com.example.youtube.UserManager.UserManager;
import com.example.youtube.R;
import com.example.youtube.RegistrationPage.RegistrationActivity2;
import com.example.youtube.videoList.VideoListActivity;

public class SignUpActivity extends AppCompatActivity {
    private UserManager userManager;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userManager = UserManager.getInstance();

        usernameEditText = findViewById(R.id.TextUserName);
        passwordEditText = findViewById(R.id.TextPassword);

        Button buttonForSignUp = findViewById(R.id.buttonForSignUp);
        buttonForSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity2.class);
            startActivity(intent);
        });

        Button buttonForHomePage = findViewById(R.id.loginButton);
        buttonForHomePage.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            boolean answerForUser = userManager.getUserName(username);
            boolean result = userManager.matchAccount(username, password);

            if (result) {
                User currentUser = null;
                for (User user : userManager.getUserList()) {
                    if (user.getUsername().equals(username)) {
                        currentUser = user;
                        break;
                    }
                }

                if (currentUser != null) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("current_user", currentUser.getUsername());
                    editor.putString("current_user_nickname", currentUser.getNickname());
                    editor.putString("current_user_image_uri", currentUser.getImageUri() != null ? currentUser.getImageUri().toString() : null);
                    editor.apply();
                }

                Intent intent = new Intent(this, VideoListActivity.class);
                startActivity(intent);
                showCustomToast("Login successfully!");
            } else {
                if (username.isEmpty() || password.isEmpty()) {
                    showCustomToast("Enter username/password");
                } else if (!answerForUser) {
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
