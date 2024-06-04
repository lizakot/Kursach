package com.example.kursach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursach.R;
import com.example.kursach.model.CategoryInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    EditText uploadTopic;
    EditText uploadDescription;
    Button uploadBadge;
    Button uploadColor;
    TextView cancelTextView;
    Button saveButton;
    private String categoryName;
    private String categoryDescription;
    private static final int REQUEST_CODE_SELECT_ICON = 101;
    private static final int REQUEST_CODE_SELECT_COLOR = 102;
    private int categoryColor = 0;
    private int categoryIcon = 0;

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
        categoryName = uploadTopic.getText().toString();
        categoryDescription = uploadDescription.getText().toString();

        saveCategoryInfo(categoryName, categoryDescription, categoryColor, categoryIcon);
    }

    private void saveCategoryInfo(String categoryName, String categoryDescription, int categoryColor, int categoryIcon) {
        String categoryId = UUID.randomUUID().toString();
        CategoryInfo categoryInfo = new CategoryInfo(categoryId, categoryName, categoryDescription, categoryColor, categoryIcon);
        String key = categoriesRef.push().getKey();
        if (key != null) {
            categoriesRef.child(key).setValue(categoryInfo);
            updateUsersCategories(categoryId);

            uploadTopic.setText("");
            uploadDescription.setText("");

            // Сохранение данных в SharedPreferences
        } else {
            Toast.makeText(UploadActivity.this, "Ошибка сохранения категории", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUsersCategories(String categoryId) {
        // Assuming you have a userId variable indicating the current user ID
        String userId;
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("categoryIds");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categoryIds = new ArrayList<>();

                // Retrieve the existing category IDs, if any
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String existingCategoryId = snapshot.getValue(String.class);
                        categoryIds.add(existingCategoryId);
                    }
                }

                // Add the new category ID to the list
                categoryIds.add(categoryId);

                // Update the user data with the updated category ID list
                userRef.setValue(categoryIds)
                        .addOnSuccessListener(aVoid -> {
                            // Update SharedPreferences after Firebase update
                            saveToSharedPreferences(categoryName, categoryDescription);
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            Toast.makeText(UploadActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void saveToSharedPreferences(String categoryName, String categoryDescription) {
        // Получение объекта SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        // Создание объекта Editor для изменения SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Сохранение данных о категории в SharedPreferences
        editor.putString("categoryName", categoryName);
        editor.putString("categoryDescription", categoryDescription);

        // Применение изменений
        editor.apply();

        // Запускаем CategoryActivity
        Intent intent = new Intent(UploadActivity.this, CategoryActivity.class);
        startActivity(intent);

        // Закрываем текущую активность
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_ICON && resultCode == RESULT_OK && data != null) {
            int selectedIcon = data.getIntExtra("selectedIcon", 0);
            if (selectedIcon != 0) {
                uploadImage.setImageResource(selectedIcon);
                categoryIcon = selectedIcon;
            }
        } else if (requestCode == REQUEST_CODE_SELECT_COLOR && resultCode == RESULT_OK && data != null) {
            int selectedColor = data.getIntExtra("selectedColor", 0);
            if (selectedColor != 0) {
                uploadImage.setColorFilter(selectedColor);
                categoryColor = selectedColor;
            }
        }
    }
}
