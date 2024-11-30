package com.dija.lecturehomework.model;

import java.sql.Timestamp;

public class ResultJoin {
    private String pname;
    private String categoryname;
    private boolean vegetarian;
    private int amount;
    private Timestamp taken;
    private Timestamp dispatched;

    // Construtores, getters e setters
    public ResultJoin(String pname, String categoryname, boolean vegetarian, int amount, Timestamp taken,Timestamp dispatched) {
        this.pname = pname;
        this.categoryname = categoryname;
        this.vegetarian = vegetarian;
        this.amount = amount;
        this.taken = taken;
        this.dispatched = dispatched;
    }

    public ResultJoin() {}

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Timestamp getDispatched() {
        return dispatched;
    }

    public void setDispatched(Timestamp dispatched) {
        this.dispatched = dispatched;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Timestamp getTaken() {
        return taken;
    }

    public void setTaken(Timestamp taken) {
        this.taken = taken;
    }
}
