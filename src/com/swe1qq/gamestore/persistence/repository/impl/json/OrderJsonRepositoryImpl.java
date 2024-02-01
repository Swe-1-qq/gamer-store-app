package com.swe1qq.gamestore.persistence.repository.impl.json;

import com.swe1qq.gamestore.persistence.entity.impl.Order;
import com.swe1qq.gamestore.persistence.repository.contracts.OrderRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

final class OrderJsonRepositoryImpl
        extends GenericJsonRepository<Order>
        implements OrderRepository {

    OrderJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.ORDERS.getPath(), TypeToken
                .getParameterized(Set.class, Order.class)
                .getType());
    }

    @Override
    public Set<Order> findAllByCustomerId(UUID customerId) {
        return entities.stream().filter(c -> c.getCustomer().getId().equals(customerId))
                .collect(Collectors.toSet());
    }
}
