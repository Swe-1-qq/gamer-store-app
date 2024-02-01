package com.swe1qq.gamestore.persistence.entity.impl;

import com.swe1qq.gamestore.persistence.entity.Entity;
import com.swe1qq.gamestore.persistence.entity.ErrorTemplates;
import com.swe1qq.gamestore.persistence.exception.EntityArgumentException;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Order extends Entity implements Comparable<Order> {

    private User customer;
    private Set<Product> products;

    private Payment payment;

    public Order(UUID id, User customer, Set<Product> products, Payment payment) {
        super(id);
        setCustomer(customer);
        this.products = products;
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        final String templateName = "клієнт";

        if (customer == null) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }

        if (!this.errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.customer = customer;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(customer, order.customer) && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customer, products);
    }

    @Override
    public int compareTo(Order o) {
        Collections.sort(products.stream().toList());
        Collections.sort(o.products.stream().toList());

        boolean productsEqual = this.products.equals(o.products);

        return customer.compareTo(o.customer) + (productsEqual ? 0 : 1);
    }
}
