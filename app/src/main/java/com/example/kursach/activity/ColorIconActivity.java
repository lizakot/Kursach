package com.example.kursach.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kursach.R;

public class ColorIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_icon);

        ImageView color1 = findViewById(R.id.color1);
        ImageView color2 = findViewById(R.id.color2);
        ImageView color3 = findViewById(R.id.color3);
        ImageView color4 = findViewById(R.id.color4);
        ImageView color5 = findViewById(R.id.color5);
        ImageView color6 = findViewById(R.id.color6);
        ImageView color7 = findViewById(R.id.color7);
        ImageView color8 = findViewById(R.id.color8);
        ImageView color9 = findViewById(R.id.color9);

        setupColorClickListener(color1, R.color.baby_pink);
        setupColorClickListener(color2, R.color.light_blue);
        setupColorClickListener(color3, R.color.mint_green);
        setupColorClickListener(color4, R.color.lavender);
        setupColorClickListener(color5, R.color.light_yellow);
        setupColorClickListener(color6, R.color.rose);
        setupColorClickListener(color7, R.color.yellow);
        setupColorClickListener(color8, R.color.light_salmon);
        setupColorClickListener(color9, R.color.cornflower_blue);
    }

    private void setupColorClickListener(ImageView imageView, final int colorResId) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedColor = getResources().getColor(colorResId);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedColor", selectedColor);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

