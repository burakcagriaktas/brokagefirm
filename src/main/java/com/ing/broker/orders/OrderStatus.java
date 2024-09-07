package com.ing.broker.orders;

import java.util.Arrays;

public enum OrderStatus {
    PENDING("PENDING", "PENDING"),
    MATCHED("MATCHED", "MATCHED"),
    CANCELED("CANCELED", "CANCELED");


    public final String label;
    public final String value;

    OrderStatus(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static boolean anyMatch(String other) {
        return Arrays
                .stream(values())
                .anyMatch(status -> status.label.equals(other));
    }

    public static OrderStatus find(String other) {
        return Arrays
                .stream(values())
                .filter(status -> status.label.equals(other))
                .toList().get(0);
    }
    @Override
    public String toString() {
        return this.value;
    }
}
