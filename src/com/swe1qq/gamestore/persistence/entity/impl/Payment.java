package com.swe1qq.gamestore.persistence.entity.impl;

import com.swe1qq.gamestore.persistence.entity.Entity;
import com.swe1qq.gamestore.persistence.entity.ErrorTemplates;
import com.swe1qq.gamestore.persistence.exception.EntityArgumentException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment extends Entity implements Comparable<Payment> {

    private User customer;
    private LocalDateTime paymentDate;
    private double amount;
    private boolean passed;

    public Payment(UUID id, User customer, LocalDateTime paymentDate, double amount, boolean passed) {
        super(id);
        setCustomer(customer);
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.passed = passed;
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(payment.amount, amount) == 0 && passed == payment.passed && Objects.equals(customer, payment.customer) && Objects.equals(paymentDate, payment.paymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customer, paymentDate, amount, passed);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "customer=" + customer +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", passed=" + passed +
                '}';
    }

    @Override
    public int compareTo(Payment o) {
        return customer.compareTo(o.customer)
                + paymentDate.compareTo(o.paymentDate)
                + (amount == o.amount ? 0 : 1)
                + (passed == o.passed ? 0 : 1);
    }
}
