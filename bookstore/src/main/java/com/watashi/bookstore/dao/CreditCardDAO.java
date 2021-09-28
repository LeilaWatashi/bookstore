package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.user.CreditCard;
import com.watashi.bookstore.entity.user.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardDAO extends DomainDAO<CreditCard> {

    List<CreditCard> findAllByCustomer(Customer customer);
    List<CreditCard> findAllByCustomerAndInactivatedTrue(Customer customer);
    List<CreditCard> findAllByCustomerAndInactivatedFalse(Customer customer);

}
