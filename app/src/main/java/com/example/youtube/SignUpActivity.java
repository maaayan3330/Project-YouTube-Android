package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // that is conect me to thr right XML
        setContentView(R.layout.activity_sign_up);

        // מציאת ה-ImageView עם ה-ID של הלוגו
        ImageView logoImage = findViewById(R.id.logoImage);

        // טעינת האנימציה מקובץ האנימציה
        Animation flipAnimation = AnimationUtils.loadAnimation(this, R.anim.flip);

        // התחלת האנימציה על הלוגו
        logoImage.startAnimation(flipAnimation);

        // here i connect the button to the next page - sign up only if the user is not have an account
        //like hemi explain
        Button buttonForSignUp = findViewById(R.id.buttonForSignUp);
        buttonForSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity2.class);
            //start a new activity
            startActivity(intent);
        });
    }
}
