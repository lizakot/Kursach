package com.example.kursach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.kursach.R;
import com.example.kursach.activity.ExpenseManager;
import com.example.kursach.activity.UploadActivity;
import com.example.kursach.adapters.CategoryAdapter;
import com.example.kursach.model.CategoryInfo;
import com.example.kursach.model.HelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    List<CategoryInfo> categoryList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);


        recyclerView.setAdapter(categoryAdapter);


        fetchUserDataFromFirebase();
        FloatingActionButton fab = view.findViewById(R.id.fab);




        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Если строка пустая, обновите список до первоначального состояния
                    categoryAdapter.updateCategoryList(categoryList);
                } else {
                    // В противном случае, примените фильтр к списку
                    categoryAdapter.filter(newText);
                }
                return true;
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), UploadActivity.class);


                startActivity(intent);
            }
        });
        return view;
    }



    private void fetchUserDataFromFirebase() {
        String userId;
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass user = dataSnapshot.getValue(HelperClass.class);
                    // Use the fetched user data here
                    if (user != null) {
                        fetchCategoriesFromFirebase(user);
                    }
                } else {
                    Log.d("FirebaseUserData", "User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
                    if (user.getCategoryIds().contains(Objects.requireNonNull(categoryInfo).getId())){
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

            }
        });
    }
}

