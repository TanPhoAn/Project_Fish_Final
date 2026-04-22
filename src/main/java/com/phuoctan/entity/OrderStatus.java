package com.phuoctan.entity;


public enum OrderStatus {
    PENDING("Pending"),
    TRANSIT("Transit"),
    COMPLETED("Completed");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
