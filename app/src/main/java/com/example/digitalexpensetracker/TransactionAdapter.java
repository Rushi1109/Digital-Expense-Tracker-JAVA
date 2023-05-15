package com.example.digitalexpensetracker;

import android.content.Context;
import android.content.Intent;
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
            holder.amount.setTextColor(this.context.getColor(R.color.red));
        }
        else{
            holder.priority.setBackgroundResource(R.drawable.green_shape);
            holder.amount.setText(model.getAmount());
            holder.amount.setTextColor(this.context.getColor(R.color.dark_green));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateTransactionActivity.class);
                intent.putExtra("id", transactionModelArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("note", transactionModelArrayList.get(holder.getAdapterPosition()).getNote());
                intent.putExtra("amount", transactionModelArrayList.get(holder.getAdapterPosition()).getAmount());
                intent.putExtra("date", transactionModelArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("category", transactionModelArrayList.get(holder.getAdapterPosition()).getCategory());
                intent.putExtra("mode", transactionModelArrayList.get(holder.getAdapterPosition()).getMode());
                intent.putExtra("type", transactionModelArrayList.get(holder.getAdapterPosition()).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note, amount, date, category, mode;
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
