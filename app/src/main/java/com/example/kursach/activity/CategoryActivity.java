package com.example.kursach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kursach.R;
import com.example.kursach.adapters.CategoryAdapter;
import com.example.kursach.model.CategoryInfo;
import com.example.kursach.model.HelperClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    List<CategoryInfo> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(CategoryActivity.this, HomeActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.reports) {
                    startActivity(new Intent(CategoryActivity.this, ReportsActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.category) {
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(CategoryActivity.this, ProfileActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.category);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);

        fetchUserDataFromFirebase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    categoryAdapter.updateCategoryList(categoryList);
                } else {categoryAdapter.filter(newText);
                }
                return true;
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteCategory(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Other existing code...
    }

    private void deleteCategory(int position) {
        // Get category ID to delete
        String categoryId = categoryList.get(position).getId();

        // Remove from Firebase
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference()
                .child("categories")
                .child(categoryId);

        categoryRef.removeValue().addOnSuccessListener(aVoid -> {
            // Remove from the list and notify adapter
            categoryList.remove(position);
            categoryAdapter.notifyItemRemoved(position);

            // Optionally, remove the category ID from the user's list of category IDs
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(userId)
                    .child("categoryIds");
            userRef.child(categoryId).removeValue();

            Toast.makeText(CategoryActivity.this, "Категория удалена", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(CategoryActivity.this, "Ошибка удаления категории", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchUserDataFromFirebase() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass user = dataSnapshot.getValue(HelperClass.class);
                    if (user != null) {
                        fetchCategoriesFromFirebase(user);
                    }
                } else {
                    Log.d("FirebaseUserData", "User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FirebaseUserData", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    private void fetchCategoriesFromFirebase(HelperClass user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryInfo categoryInfo = snapshot.getValue(CategoryInfo.class);
                    if (user.getCategoryIds().contains(Objects.requireNonNull(categoryInfo).getId())) {
                        categoryList.add(categoryInfo);
                    }

                    Log.d("FirebaseData", "Category Name: " + categoryInfo.getCategoryName());
                    Log.d("FirebaseData", "Category Description: " + categoryInfo.getCategoryDescription());
                    Log.d("FirebaseData", "Category Color: " + categoryInfo.getCategoryColor());
                    Log.d("FirebaseData", "Category Icon: " + categoryInfo.getCategoryIcon());
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FirebaseData", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}