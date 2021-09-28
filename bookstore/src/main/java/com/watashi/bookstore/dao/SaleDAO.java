package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.shop.Sale;
import com.watashi.bookstore.entity.user.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDAO extends AssociativeDomainDAO<Sale>, DateFilter<Sale>{
    List<Sale> findAllByCustomer(Customer customer);
}
