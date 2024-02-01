package com.swe1qq.gamestore.domain.impl;

import com.swe1qq.gamestore.domain.contract.OrderService;
import com.swe1qq.gamestore.persistence.entity.impl.Order;
import com.swe1qq.gamestore.persistence.repository.contracts.OrderRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;

final class OrderServiceImpl
        extends GenericServiceImpl<Order>
        implements OrderService {

    private final OrderRepository orderRepository;

    OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    @Override
    public Set<Order> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Order> getAll(Predicate<Order> filter) {
        return new TreeSet<>(orderRepository.findAll(filter));
    }

    @Override
    public Set<Order> findAllByCustomerId(UUID customerId) {
        return new HashSet<>(orderRepository.findAllByCustomerId(customerId));
    }
}
