package com.example.kursach.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentKt;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.kursach.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_graphh);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView navigationBar = findViewById(R.id.bottomNavigationView);


        NavigationUI.setupWithNavController(navigationBar, navController);
    }
}
