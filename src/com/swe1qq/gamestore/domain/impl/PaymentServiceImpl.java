package com.swe1qq.gamestore.domain.impl;

import com.swe1qq.gamestore.domain.contract.PaymentService;
import com.swe1qq.gamestore.persistence.entity.impl.Payment;
import com.swe1qq.gamestore.persistence.repository.contracts.PaymentRepository;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;

final class PaymentServiceImpl
        extends GenericServiceImpl<Payment>
        implements PaymentService {

    private final PaymentRepository postRepository;

    PaymentServiceImpl(PaymentRepository postRepository) {
        super(postRepository);
        this.postRepository = postRepository;
    }

    @Override
    public Set<Payment> getAll() {
        return getAll(p -> true);
    }

    @Override
    public Set<Payment> getAll(Predicate<Payment> filter) {
        return new TreeSet<>(postRepository.findAll(filter));
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
