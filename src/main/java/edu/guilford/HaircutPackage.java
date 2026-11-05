package edu.guilford;

import java.math.BigDecimal;

public class HaircutPackage {
    private final String name;
    private final BigDecimal price;

    public HaircutPackage(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
