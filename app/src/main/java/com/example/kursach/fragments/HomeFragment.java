package com.example.kursach.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.activity.ExpenseManager;
import com.example.kursach.activity.UploadActivity;
import com.example.kursach.adapters.ExpenseAdapter;
import com.example.kursach.viewmodels.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView balanceTextView;
    private ExpenseAdapter expenseAdapter;
    private HomeViewModel viewModel;

    private DatabaseReference userBalanceRef;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        balanceTextView = view.findViewById(R.id.balanceTextView);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        // Получение баланса из ViewModel и установка его в TextView
        viewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            if (balance != null) {
                String balanceText = "Текущий баланс: " + balance + " BYN";
                balanceTextView.setText(balanceText);
                Log.d("HomeFragment", "Баланс из базы данных: " + balance);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);

        // Инициализация Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Получение ссылки на узел баланса текущего пользователя
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            userBalanceRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("balance");

            userBalanceRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Получаем значение баланса из базы данных
                        int balance = snapshot.getValue(Integer.class);

                        // Устанавливаем новое значение баланса в ViewModel
                        viewModel.setBalance(balance);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Обработка ошибок
                }
            });
        }

        observeViewModel();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ExpenseManager.class);


                startActivity(intent);
            }
        });


        return view;
    }


    private void observeViewModel() {
        viewModel.getExpenseList().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                expenseAdapter.setExpenseList(expenses);
                expenseAdapter.notifyDataSetChanged();
            }
        });
    }
}
