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
import com.example.kursach.model.CategoryInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    EditText uploadTopic;
    EditText uploadDescription;
    Button uploadBadge;
    Button uploadColor;
    TextView cancelTextView;
    Button saveButton;
    private static final int REQUEST_CODE_SELECT_ICON = 101;
    private static final int REQUEST_CODE_SELECT_COLOR = 102;

    DatabaseReference categoriesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadTopic = findViewById(R.id.uploadTopic);
        uploadDescription = findViewById(R.id.uploadDescription);
        uploadBadge = findViewById(R.id.uploadBadge);
        uploadColor = findViewById(R.id.uploadColor);
        cancelTextView = findViewById(R.id.cancelTextView);
        saveButton = findViewById(R.id.saveButton);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference().child("categories");

        uploadBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, ChooseIconActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_ICON);
            }
        });
        uploadColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, ColorIconActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_COLOR);
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Закрываем текущую активность
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });
    }

    private void onSaveClicked() {
        String categoryName = uploadTopic.getText().toString();
        String categoryDescription = uploadDescription.getText().toString();

        saveCategoryInfo(categoryName, categoryDescription);
    }

    private void saveCategoryInfo(String categoryName, String categoryDescription) {
        CategoryInfo categoryInfo = new CategoryInfo(categoryName, categoryDescription);

        String key = categoriesRef.push().getKey();
        if (key != null) {
            categoriesRef.child(key).setValue(categoryInfo);
            Toast.makeText(UploadActivity.this, "Категория сохранена", Toast.LENGTH_SHORT).show();
            uploadTopic.setText("");
            uploadDescription.setText("");
        } else {
            Toast.makeText(UploadActivity.this, "Ошибка сохранения категории", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_ICON && resultCode == RESULT_OK && data != null) {
            int selectedIcon = data.getIntExtra("selectedIcon", 0);
            if (selectedIcon != 0) {
                uploadImage.setImageResource(selectedIcon);
            }
        } else if (requestCode == REQUEST_CODE_SELECT_COLOR && resultCode == RESULT_OK && data != null) {
            int selectedColor = data.getIntExtra("selectedColor", 0);
            if (selectedColor != 0) {
                uploadImage.setColorFilter(selectedColor);
            }
        }
    }
}
