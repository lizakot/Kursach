package com.example.kursach.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kursach.R;
import com.example.kursach.adapters.ExpenseAdapter;
import com.example.kursach.viewmodels.HomeViewModel;


public class HomeFragment extends Fragment {

        private RecyclerView recyclerView;
        private ExpenseAdapter expenseAdapter;
        private HomeViewModel viewModel;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            observeViewModel();

            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            expenseAdapter = new ExpenseAdapter();
            recyclerView.setAdapter(expenseAdapter);

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
