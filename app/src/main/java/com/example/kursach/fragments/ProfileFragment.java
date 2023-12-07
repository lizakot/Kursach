package com.example.kursach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kursach.R;
import com.example.kursach.activity.EditProfileActivity;
import com.example.kursach.activity.MainActivity;

public class ProfileFragment extends Fragment {

    TextView profileName, profileEmail, profileUsername, profilePassword;
    TextView titleName;
    Button editButton;
    ImageButton logoutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profilePassword = view.findViewById(R.id.profilePassword);
        titleName = view.findViewById(R.id.titleName);
        editButton = view.findViewById(R.id.editButton);
        logoutButton = view.findViewById(R.id.logoutButton);


        SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

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
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
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
                SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();

                // Переход на MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish(); // Закрыть текущую активность
            }
        });

        return view;
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
