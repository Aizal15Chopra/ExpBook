package com.example.aizal_ExpBook;

import java.util.UUID;

public class Expense {
    private String id;
    private String name;
    private double amount;
    private String date;
    private String comment;

    public Expense(String name, double amount, String date, String comment) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }
    public String getId() {return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nAmount: $" + amount + "\nDate: " + date + "\nComment: " + comment;
    }

}

