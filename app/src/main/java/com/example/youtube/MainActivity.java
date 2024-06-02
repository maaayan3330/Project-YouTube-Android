package com.example.youtube;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // מציאת ה-ImageView עם ה-ID של הלוגו
        ImageView logoImage = findViewById(R.id.logoImage);

        // טעינת האנימציה מקובץ האנימציה
        Animation flipAnimation = AnimationUtils.loadAnimation(this, R.anim.flip);

        // התחלת האנימציה על הלוגו
        logoImage.startAnimation(flipAnimation);
    }
}
