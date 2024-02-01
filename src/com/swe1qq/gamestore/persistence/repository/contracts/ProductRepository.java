package com.swe1qq.gamestore.persistence.repository.contracts;

import com.swe1qq.gamestore.persistence.entity.impl.Product;
import com.swe1qq.gamestore.persistence.repository.Repository;

import java.util.Set;

public interface ProductRepository extends Repository<Product> {

    Set<Product> findAllByType(Product.ProductType type);
    Product findByName(String name);
}
