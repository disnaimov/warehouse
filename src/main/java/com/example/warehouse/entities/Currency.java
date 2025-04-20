package com.example.warehouse.entities;

public enum Currency {
    RUB,
    CNY,
    USD,
    EUR;

    public static Currency fromString(String currencyString) {
        if (currencyString == null) {
            throw new IllegalArgumentException("currencyString cannot be null");
        }

        try {
            return Currency.valueOf(currencyString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RUB;
        }
    }
}
