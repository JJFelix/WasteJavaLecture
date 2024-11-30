package com.dija.lecturehomework.model;

public class Position {
    private String currencyPair;
    private String direction;
    private double quantity;

    // Construtor
    public Position(String currencyPair, String direction, double quantity) {
        this.currencyPair = currencyPair;
        this.direction = direction;
        this.quantity = quantity;
    }

    // Getters e setters
    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
