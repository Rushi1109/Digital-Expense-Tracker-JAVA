package com.example.digitalexpensetracker;

public class TransactionModel {
    private String amount, category, date, id, mode, note, type;

    public TransactionModel(){
    }

    public TransactionModel(String id, String amount, String note, String category, String date, String mode, String type) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.id = id;
        this.mode = mode;
        this.note = note;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
