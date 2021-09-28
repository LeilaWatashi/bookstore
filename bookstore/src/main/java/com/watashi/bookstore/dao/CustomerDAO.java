package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDAO extends DomainDAO<Customer> {

    Optional<Customer> findCustomerByUser(User user);

    Optional<Customer> findCustomerByUserAndInactivatedFalse(User user);

    Optional<Customer> findCustomerByUserAndInactivatedTrue(User user);
}
