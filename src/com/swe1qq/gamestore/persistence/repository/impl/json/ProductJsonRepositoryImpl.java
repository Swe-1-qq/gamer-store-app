package com.swe1qq.gamestore.persistence.repository.impl.json;

import com.swe1qq.gamestore.persistence.entity.impl.Product;
import com.swe1qq.gamestore.persistence.repository.contracts.ProductRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Set;
import java.util.stream.Collectors;

final class ProductJsonRepositoryImpl
        extends GenericJsonRepository<Product>
        implements ProductRepository {

    ProductJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.PRODUCTS.getPath(), TypeToken
                .getParameterized(Set.class, Product.class)
                .getType());
    }

    @Override
    public Set<Product> findAllByType(Product.ProductType type) {
        return entities.stream().filter(c -> c.getType().equals(type))
                .collect(Collectors.toSet());
    }

    @Override
    public Product findByName(String name) {
        return entities.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
