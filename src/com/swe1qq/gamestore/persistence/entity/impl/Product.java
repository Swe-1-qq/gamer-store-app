package com.swe1qq.gamestore.persistence.entity.impl;

import com.swe1qq.gamestore.persistence.entity.Entity;
import com.swe1qq.gamestore.persistence.entity.ErrorTemplates;
import com.swe1qq.gamestore.persistence.exception.EntityArgumentException;

import java.util.Objects;
import java.util.UUID;

public class Product extends Entity implements Comparable<Product> {

    private ProductType type;
    private String name;
    private String description;
    private double price;
    private int quantity;

    public Product(UUID id, String type, String name, String description, double price, int quantity) {
        super(id);
        setType(type);
        setName(name);
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(String type) {
        final String templateName = "тип";

        if (type.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }

        ProductType result = null;

        try {
            result = ProductType.fromName(type);
        } catch (IllegalArgumentException ex) {
            errors.add(ErrorTemplates.WRONG_TYPE.getTemplate().formatted(templateName));
        }

        if (!this.errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.type = result;
    }

    public String getName() {
        return name;
    }

    /**
     * Setter method for the name with validation.
     * <p>
     * This method sets the name with the specified validation criteria: - Must not be empty. -
     * Must be longer than 1 character. - Must be shorter than 24 characters. - Must consist only
     * of Latin letters.
     *
     * @param name the name to be set, must meet the validation criteria
     * @throws IllegalArgumentException if the provided name does not meet the validation
     *                                  criteria
     */
    public void setName(String name) {
        final String templateName = "імʼя";

        if (name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (name.length() < 1) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted(templateName, 1));
        }
        if (name.length() > 24) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 24));
        }

        if (!this.errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name) + this.type.compareTo(o.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(product.price, price) == 0 && quantity == product.quantity
                && type == product.type && Objects.equals(name, product.name)
                && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, name, description, price, quantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    public enum ProductType {
        KEYBOARD("Клавіатура"),
        HARDWARE("Апаратура"),
        MOUSE("Мишка"),
        HEADPHONES("Навушники"),
        GAMEPAD("Джойстик"),
        SOFTWARE("ПЗ"),
        MONITOR("Монітор");

        private final String name;

        ProductType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ProductType fromName(String name) {
            for (ProductType productType : ProductType.values()) {
                if (productType.name.equalsIgnoreCase(name)) {
                    return productType;
                }
            }

            throw new IllegalArgumentException("Enum value with this name doesn't exist.");
        }
    }
}
