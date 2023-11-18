package com.example.kursach.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursach.R;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    EditText uploadTopic;
    Button uploadBadge;
    Button uploadColor;
    TextView cancelTextView;
    Button saveButton;
    private static final int REQUEST_CODE_SELECT_ICON = 101; // Любое уникальное число

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadTopic = findViewById(R.id.uploadTopic);
        uploadBadge = findViewById(R.id.uploadBadge);
        uploadColor = findViewById(R.id.uploadColor);
        cancelTextView = findViewById(R.id.cancelTextView);
        saveButton = findViewById(R.id.saveButton);

        uploadBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, ChooseIconActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_ICON);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_ICON && resultCode == RESULT_OK && data != null) {
            int selectedIcon = data.getIntExtra("selectedIcon", 0);
            if (selectedIcon != 0) {
                uploadImage.setImageResource(selectedIcon);
            }
        }
    }
}

