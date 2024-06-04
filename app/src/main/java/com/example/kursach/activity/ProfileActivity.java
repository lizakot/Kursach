package com.example.kursach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kursach.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileEmail, profileUsername, profilePassword;
    TextView titleName;
    Button editButton;
    ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.reports) {
                    startActivity(new Intent(ProfileActivity.this, ReportsActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.category) {
                    // Переход на CategoryActivity при выборе "Категории"
                    startActivity(new Intent(ProfileActivity.this, CategoryActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.profile) {
                    // Переход на ProfileActivity при выборе "Профиль"

                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);


        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        String name = preferences.getString("name", "");
        String email = preferences.getString("email", "");
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        titleName.setText(name);
        profileName.setText(name);
        profileEmail.setText(email);
        profileUsername.setText(username);
        profilePassword.setText(password);

        Log.d("ProfileFragment", "Name: " + name);
        Log.d("ProfileFragment", "Email: " + email);
        Log.d("ProfileFragment", "Username: " + username);
        Log.d("ProfileFragment", "Password: " + password);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("name", profileName.getText().toString());
                intent.putExtra("email", profileEmail.getText().toString());
                intent.putExtra("username", profileUsername.getText().toString());
                intent.putExtra("password", profilePassword.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Очистка данных пользователя в SharedPreferences
                SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();

                // Переход на MainActivity
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Закрыть текущую активность
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                if (data != null) {
                    String editedName = data.getStringExtra("edited_name");
                    String editedEmail = data.getStringExtra("edited_email");
                    String editedUsername = data.getStringExtra("edited_username");
                    String editedPassword = data.getStringExtra("edited_password");

                    // Обновление данных в UI
                    titleName.setText(editedName);
                    profileName.setText(editedName);
                    profileEmail.setText(editedEmail);
                    profileUsername.setText(editedUsername);
                    profilePassword.setText(editedPassword);
                }
            }
        }
    }

}