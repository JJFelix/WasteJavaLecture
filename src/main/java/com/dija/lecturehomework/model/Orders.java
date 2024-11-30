package com.dija.lecturehomework.model;

import java.time.LocalDateTime;

public class Orders {
    private int id;
    private String pizzaname;
    private int amount;
    private LocalDateTime taken;
    private LocalDateTime dispatched;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPizzaname() {
        return pizzaname;
    }

    public void setPizzaname(String pizzaname) {
        this.pizzaname = pizzaname;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getTaken() {
        return taken;
    }

    public void setTaken(LocalDateTime taken) {
        this.taken = taken;
    }

    public LocalDateTime getDispatched() {
        return dispatched;
    }

    public void setDispatched(LocalDateTime dispatched) {
        this.dispatched = dispatched;
    }
}
