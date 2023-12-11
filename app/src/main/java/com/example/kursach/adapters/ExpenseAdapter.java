package com.example.kursach.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private Context context;
    private List<Expense> expenseList;


    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    public String getExpenseId(int position) {
        return expenseList.get(position).getId(); 
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_item_layout, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.expenseNameTextView.setText(expense.getCategoryId());
        holder.expenseAmountTextView.setText("Сумма: " + String.valueOf(expense.getAmount()) + " BYN");
        holder.expenseDateTextView.setText("Дата: " + expense.getDate());
        holder.categoryIconImageView.setImageResource(expense.getCategoryIcon());
        holder.categoryIconImageView.setColorFilter(expense.getCategoryColor());

    }
    public void removeItem(int position) {
        expenseList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseNameTextView;
        TextView expenseAmountTextView;
        TextView expenseDateTextView;

        ImageView categoryIconImageView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseAmountTextView = itemView.findViewById(R.id.expenseAmountTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
            categoryIconImageView = itemView.findViewById(R.id.expenseIconImageView);
        }
    }
}


