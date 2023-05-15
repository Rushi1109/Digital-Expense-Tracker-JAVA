package com.example.digitalexpensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{
    Context context;
    ArrayList<TransactionModel> transactionModelArrayList;

    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModelArrayList) {
        this.context = context;
        this.transactionModelArrayList = transactionModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransactionModel model = transactionModelArrayList.get(position);

        holder.note.setText(model.getNote());
        holder.date.setText(model.getDate());
        holder.category.setText(model.getCategory());
        holder.mode.setText(model.getMode());
        String priority = model.getType();

        if(priority.equals("Expense")){
            holder.priority.setBackgroundResource(R.drawable.red_shape);
            holder.amount.setText(model.getAmount());
            holder.amount.setTextColor(this.context.getResources().getColor(R.color.red));
        }
        else{
            holder.priority.setBackgroundResource(R.drawable.green_shape);
            holder.amount.setText(model.getAmount());
            holder.amount.setTextColor(this.context.getResources().getColor(R.color.dark_green));
        }
    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note, amount, date, category, type, mode;
        View priority;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            priority = itemView.findViewById(R.id.priority_one);
            note = itemView.findViewById(R.id.note_one);
            amount = itemView.findViewById(R.id.amount_one);
            date = itemView.findViewById(R.id.date_one);
            category = itemView.findViewById(R.id.category_one);
            mode = itemView.findViewById(R.id.expense_mode_one);
        }
    }
}
