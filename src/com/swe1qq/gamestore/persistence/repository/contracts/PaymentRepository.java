package com.swe1qq.gamestore.persistence.repository.contracts;

import com.swe1qq.gamestore.persistence.entity.impl.Payment;
import com.swe1qq.gamestore.persistence.repository.Repository;

import java.util.Set;
import java.util.UUID;

public interface PaymentRepository extends Repository<Payment> {

    Set<Payment> findAllPassed();

    Set<Payment> findAllByCustomerId(UUID customerId);
}
