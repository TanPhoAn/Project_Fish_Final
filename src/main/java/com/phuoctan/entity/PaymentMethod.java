package com.phuoctan.entity;



public enum PaymentMethod {
    COD("cod"),
    BANK("bank"),;

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PaymentMethod fromString(Object value) {
        if (value == null) return null;
        for (PaymentMethod m : PaymentMethod.values()) {
            if (m.name().equalsIgnoreCase(value.toString()) ||
                    m.label.equalsIgnoreCase(value.toString())) {
                return m;
            }
        }
        return null;
    }
}
