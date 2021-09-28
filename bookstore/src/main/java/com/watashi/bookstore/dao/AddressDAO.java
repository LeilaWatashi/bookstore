package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.user.Address;
import com.watashi.bookstore.entity.user.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDAO extends DomainDAO<Address> {

    List<Address> findAllByCustomer(Customer customer);
    List<Address> findAllByCustomerAndInactivatedTrue(Customer customer);
    List<Address> findAllByCustomerAndInactivatedFalse(Customer customer);
}
