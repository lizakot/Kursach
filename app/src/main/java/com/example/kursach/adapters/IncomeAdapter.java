package com.example.kursach.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.activity.HomeActivity;
import com.example.kursach.model.Income;

import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private List<Income> incomeList;

    public IncomeAdapter(Context context, List<Income> incomeList) {
        this.incomeList = incomeList;
    }

    public String getIncomeId(int position) {
        return incomeList.get(position).getId(); // Предположим, что у Income есть метод getId() для получения его идентификатора
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item_layout, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income income = incomeList.get(position);

        holder.amountTextView.setText("Сумма: " + income.getAmount() + " BYN");
        holder.dateTextView.setText("Дата: " + income.getDate());
        holder.descriptionTextView.setText(income.getDescription());
    }

    public void removeItem(int position) {
        incomeList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    static class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView;
        TextView dateTextView;
        TextView descriptionTextView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.IncomeAmountTextView);
            dateTextView = itemView.findViewById(R.id.IncomeDateTextView);
            descriptionTextView = itemView.findViewById(R.id.IncomeDescriptionTextView);
        }
    }
}