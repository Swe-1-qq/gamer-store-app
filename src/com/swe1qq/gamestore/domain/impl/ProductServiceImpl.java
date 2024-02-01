package com.swe1qq.gamestore.domain.impl;

import com.swe1qq.gamestore.domain.contract.ProductService;
import com.swe1qq.gamestore.persistence.entity.impl.Product;
import com.swe1qq.gamestore.persistence.repository.contracts.ProductRepository;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

final class ProductServiceImpl
        extends GenericServiceImpl<Product>
        implements ProductService {

    private final ProductRepository productRepository;

    ProductServiceImpl(ProductRepository commentRepository) {
        super(commentRepository);
        this.productRepository = commentRepository;
    }

    @Override
    public Set<Product> getAllByType(Product.ProductType type) {
        return productRepository.findAllByType(type);
    }

    @Override
    public Product getByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Set<Product> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Product> getAll(Predicate<Product> filter) {
        return new TreeSet<>(productRepository.findAll(filter));
    }
}
