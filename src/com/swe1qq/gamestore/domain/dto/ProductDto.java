package com.swe1qq.gamestore.domain.dto;

import com.swe1qq.gamestore.persistence.entity.Entity;

import java.util.UUID;

public final class ProductDto extends Entity {

    private final String type;
    private final String name;
    private final String description;
    private final double price;
    private final int quantity;


    public ProductDto(UUID id,
                      String type,
                      String name,
                      String description,
                      double price,
                      int quantity) {
        super(id);
        this.type = type;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }


    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
