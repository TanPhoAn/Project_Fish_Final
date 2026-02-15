package com.phuoctan.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumeratedValue;

public enum ProductCategory {
    FISH("Fish"),
    PLANT("Plant"),
    FISH_FOOD("Fish Food"),
    FISH_TANK("Fish Tank"),
    AQUATIC_ACCESSORY("Accessory");

    private final String label;

    ProductCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

