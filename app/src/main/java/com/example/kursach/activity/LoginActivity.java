package com.example.kursach.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kursach.R;
import com.example.kursach.viewmodels.LoginViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    LoginViewModel loginViewModel;
    String userId;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

            loginUsername = findViewById(R.id.login_username);
            loginPassword = findViewById(R.id.login_password);
            loginButton = findViewById(R.id.login_button);
            signupRedirectText = findViewById(R.id.signupRedirectText);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (loginViewModel.getUsernameError().getValue() == null &&
                            loginViewModel.getPasswordError().getValue() == null) {
                        checkUser();
                    } else {
                        // Отображение ошибок без использования Toast
                        if (loginViewModel.getUsernameError().getValue() != null) {
                            loginUsername.setError(loginViewModel.getUsernameError().getValue());
                        }

                        if (loginViewModel.getPasswordError().getValue() != null) {
                            loginPassword.setError(loginViewModel.getPasswordError().getValue());
                        }
                    }
                }
            });

            signupRedirectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });

            // Наблюдение за изменениями в валидации
            loginViewModel.getUsernameError().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String errorMessage) {
                    // Обработка изменений в ошибке валидации имени пользователя
                }
            });

            loginViewModel.getPasswordError().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String errorMessage) {
                    // Обработка изменений в ошибке валидации пароля
                }
            });
        }
    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginUsername.setError(null);

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        String userIdFromDB = userSnapshot.getKey();

                        Log.d("MyApp", "Полученный пароль из БД: " + passwordFromDB);
                        Log.d("MyApp", "Полученное имя пользователя из БД: " + userSnapshot.child("username").getValue(String.class));



                        if (passwordFromDB.equals(userPassword)) {
                            // Данные пользователя найдены, сохраняем их в SharedPreferences
                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String emailFromDB = userSnapshot.child("email").getValue(String.class);
                            String usernameFromDB = userSnapshot.child("username").getValue(String.class);

                            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userId", userIdFromDB);
                            editor.putString("name", nameFromDB);
                            editor.putString("email", emailFromDB);
                            editor.putString("username", usernameFromDB);
                            editor.putString("password", passwordFromDB);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            loginPassword.setError("Неверный пароль");
                        }
                    }
                } else {
                    loginUsername.setError("Пользователь не существует");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибок
            }
        });
    }

}
