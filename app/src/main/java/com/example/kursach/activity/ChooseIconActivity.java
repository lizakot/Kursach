package com.example.kursach.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kursach.R;

public class ChooseIconActivity extends AppCompatActivity {

    ImageView icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9, icon10, icon11;
    ImageView uploadImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_icon);

        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        icon5 = findViewById(R.id.icon5);
        icon6 = findViewById(R.id.icon6);
        icon7 = findViewById(R.id.icon7);
        icon8 = findViewById(R.id.icon8);
        icon9 = findViewById(R.id.icon9);
        icon10 = findViewById(R.id.icon10);
        icon11 = findViewById(R.id.icon11);

        uploadImage = findViewById(R.id.uploadImage);

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedIcon", R.drawable.icon_gallery_coffee);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedIcon", R.drawable.icon_gallery_airplane);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedIcon", R.drawable.icon_gallery_shopping);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedIcon", R.drawable.icon_gallery_car);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedIcon", R.drawable.icon_gallery_products);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        icon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedIcon", R.drawable.icon_gallery_pets);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        icon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedIcon", R.drawable.icon_gallery_apteka);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        icon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedIcon", R.drawable.icon_gallery_museum);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        icon9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedIcon", R.drawable.icon_gallery_food);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        icon10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedIcon", R.drawable.icon_gallery_entertainment);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        icon11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedIcon", R.drawable.icon_gallery_sports_);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });




    }
}