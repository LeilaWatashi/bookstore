package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.product.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends DomainDAO<Product> {
}
