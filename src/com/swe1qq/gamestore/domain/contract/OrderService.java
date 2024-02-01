package com.swe1qq.gamestore.domain.contract;

import com.swe1qq.gamestore.domain.Service;
import com.swe1qq.gamestore.persistence.entity.impl.Order;

import java.util.Set;
import java.util.UUID;

public interface OrderService extends Service<Order> {

    Set<Order> findAllByCustomerId(UUID customerId);

}
