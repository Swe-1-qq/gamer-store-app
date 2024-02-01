package com.swe1qq.gamestore.persistence.repository.contracts;

import com.swe1qq.gamestore.persistence.entity.impl.Order;
import com.swe1qq.gamestore.persistence.repository.Repository;

import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends Repository<Order> {

    Set<Order> findAllByCustomerId(UUID customerId);
}
