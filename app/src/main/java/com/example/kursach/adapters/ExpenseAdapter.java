package com.example.kursach.adapters;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import java.util.List;
        import com.example.kursach.R;
        import com.example.kursach.model.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public void setExpenseList(List<Expense> expenses) {
        this.expenseList = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item_layout, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.expenseAmountTextView.setText("Сумма: " + expense.getAmount() + "BYN");
        holder.expenseDateTextView.setText("Дата: " + expense.getDate());
    }

    @Override
    public int getItemCount() {
        return expenseList == null ? 0 : expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseNameTextView;
        TextView expenseAmountTextView;
        TextView expenseDateTextView;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseAmountTextView = itemView.findViewById(R.id.expenseAmountTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
        }
    }
}


