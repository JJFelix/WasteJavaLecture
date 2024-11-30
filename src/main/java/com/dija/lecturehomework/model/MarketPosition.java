package com.dija.lecturehomework.model;

public class MarketPosition {
    private String instrument;
    private int quantity;
    private double unrealizedPL; // Altere para double

    // Construtor
    public MarketPosition(String instrument, int quantity, double unrealizedPL) {
        this.instrument = instrument;
        this.quantity = quantity;
        this.unrealizedPL = unrealizedPL;
    }

    // Getters e Setters
    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnrealizedPL() { // Getter para double
        return unrealizedPL;
    }

    public void setUnrealizedPL(double unrealizedPL) { // Setter para double
        this.unrealizedPL = unrealizedPL;
    }
}
