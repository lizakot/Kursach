package com.example.kursach.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kursach.R;
import com.example.kursach.model.HelperClass;
import com.example.kursach.viewmodels.SignupViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    private TextView textError1, textError2, textError3, textError4;
    private SignupViewModel signupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        textError1 = findViewById(R.id.textError1);
        textError2 = findViewById(R.id.textError2);
        textError3 = findViewById(R.id.textError3);
        textError4 = findViewById(R.id.textError4);

        // Определение наблюдателей для обновления UI в соответствии с результатами валидации
        signupViewModel.getIsNameValid().observe(this, isNameValid -> {
            textError1.setVisibility(isNameValid ? View.GONE : View.VISIBLE);
        });

        signupViewModel.getIsEmailValid().observe(this, isEmailValid -> {
            textError2.setVisibility(isEmailValid ? View.GONE : View.VISIBLE);
        });

        signupViewModel.getIsUsernameValid().observe(this, isUsernameValid -> {
            textError3.setVisibility(isUsernameValid ? View.GONE : View.VISIBLE);
        });

        signupViewModel.getIsPasswordValid().observe(this, isPasswordValid -> {
            textError4.setVisibility(isPasswordValid ? View.GONE : View.VISIBLE);
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    String name = signupName.getText().toString();
                    String email = signupEmail.getText().toString();
                    String username = signupUsername.getText().toString();
                    String password = signupPassword.getText().toString();
                    List<String> categoryIds = new ArrayList<>();
                    categoryIds.add("");
                    String userId = reference.push().getKey(); // Генерация уникального ID для нового пользователя

                    HelperClass helperClass = new HelperClass(categoryIds,userId, name, email, username, password);


                    reference.child(userId).setValue(helperClass);



                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateFields() {
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();

        signupViewModel.validateName(name);
        signupViewModel.validateEmail(email);
        signupViewModel.validateUsername(username);
        signupViewModel.validatePassword(password);

        return signupViewModel.getIsNameValid().getValue() &&
                signupViewModel.getIsEmailValid().getValue() &&
                signupViewModel.getIsUsernameValid().getValue() &&
                signupViewModel.getIsPasswordValid().getValue();
    }
}

