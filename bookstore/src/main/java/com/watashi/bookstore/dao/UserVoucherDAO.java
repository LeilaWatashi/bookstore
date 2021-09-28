package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.shop.Voucher;
import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.UserVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVoucherDAO extends AssociativeDomainDAO<UserVoucher> {

    List<UserVoucher> findCustomerVoucherByCustomer(Customer customer);
    List<UserVoucher> findCustomerVoucherByUsedFalseAndCustomer(Customer customer);
    Optional<UserVoucher> findByUsedFalseAndCustomerAndVoucher(Customer customer, Voucher voucher);

}
