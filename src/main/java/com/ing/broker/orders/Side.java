package com.ing.broker.orders;

import java.util.Arrays;

public enum Side {
    BUY("BUY", "BUY"),
    SELL("SELL", "SELL");

    public final String label;
    public final String value;

    Side(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static boolean anyMatch(String otherSide) {
        return Arrays
                .stream(values())
                .anyMatch(side -> side.label.equals(otherSide));
    }

    public static Side find(String otherSide) {
        return Arrays
                .stream(values())
                .filter(side -> side.label.equals(otherSide))
                .toList().get(0);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
