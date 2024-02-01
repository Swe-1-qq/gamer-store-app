package com.swe1qq.gamestore.domain.contract;

import com.swe1qq.gamestore.domain.Service;
import com.swe1qq.gamestore.persistence.entity.impl.Product;

import java.util.Set;

public interface ProductService extends Service<Product> {

    Set<Product> getAllByType(Product.ProductType type);
    Product getByName(String name);
}
