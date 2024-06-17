package com.example.youtube.RegistrationPage;

import android.content.Context;
import com.example.youtube.UserManager.UserManager;
import com.example.youtube.R;

// This class makes sure that all the fields are filled and filled correctly

public class PasswordValidator {
    private UserManager userManager;

    public PasswordValidator() {
        userManager = UserManager.getInstance(); //  UserManager
    }

    // this func checks if the fields have been filled correctly
    public String registerUser(Context context, String username, String password, String confirmPassword, String nickname) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nickname.isEmpty()) {
            return "All fields must be filled";
        }

        if (!isPasswordValid(password)) {
            return context.getString(R.string.password_restrictions);
        }

        if (!password.equals(confirmPassword)) {
            return "The passwords do not match";
        }

        // if all the fields are correct we will register the user account if he clicked the button
        return "User registered successfully";
    }

    // this func is for checking if the password is valid
    public boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
}
