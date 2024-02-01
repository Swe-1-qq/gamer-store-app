package com.swe1qq.gamestore.persistence.repository.impl.json;

import com.swe1qq.gamestore.persistence.entity.impl.Payment;
import com.swe1qq.gamestore.persistence.repository.contracts.PaymentRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Set;
import java.util.UUID;

final class PaymentJsonRepositoryImpl
        extends GenericJsonRepository<Payment>
        implements PaymentRepository {

    PaymentJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.PAYMENTS.getPath(), TypeToken
                .getParameterized(Set.class, Payment.class)
                .getType());
    }

    @Override
    public Set<Payment> findAllPassed() {
        return null;
    }

    @Override
    public Set<Payment> findAllByCustomerId(UUID customerId) {
        return null;
    }
}
