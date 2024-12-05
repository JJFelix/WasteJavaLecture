package com.dija.lecturehomework.model;

import java.sql.Timestamp;

public class ResultJoin2 {
    private String sdate;
    private String demand;
    private int servid;
    private int quantity;
    private String wtype;
    private String description;

    // Construtores, getters e setters
    public ResultJoin2(String sdate, String demand, int servid, int quantity, String wtype, String description) {
        this.sdate = sdate;
        this.demand = demand;
        this.servid = servid;
        this.quantity = quantity;
        this.wtype = wtype;
        this.description = description;
    }

    public ResultJoin2() {}

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public int getServid() {
        return servid;
    }

    public void setServid(int servid) {
        this.servid = servid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getWtype() {
        return wtype;
    }

    public void setWtype(String wtype) {
        this.wtype = wtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
