package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.shop.Trade;
import com.watashi.bookstore.entity.user.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeDAO extends AssociativeDomainDAO<Trade>, DateFilter<Trade> {
    List<Trade> findAllByOrderCustomer(Customer customer);
}
