package com.swe1qq.gamestore.domain.contract;

import com.swe1qq.gamestore.domain.Service;
import com.swe1qq.gamestore.persistence.entity.impl.Payment;

import java.util.Set;
import java.util.UUID;

public interface PaymentService extends Service<Payment> {

    Set<Payment> findAllPassed();

    Set<Payment> findAllByCustomerId(UUID customerId);
}
