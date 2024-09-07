package com.ing.broker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Side {
    BUY("BUY"),
    SELL("SELL");

    public final String label;

    Side(String label) {
        this.label= label;
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
}
