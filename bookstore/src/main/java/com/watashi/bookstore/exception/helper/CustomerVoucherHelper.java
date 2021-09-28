package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.shop.Voucher;
import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.UserVoucher;
import com.watashi.bookstore.entity.user.UserVoucherId;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerVoucherHelper {

    private final IDomainService<Voucher> domainService;
    private final Voucher mockVoucher;

    private ObjectProvider<UserVoucher> customerVoucherObjectProvider;
    private ObjectProvider<UserVoucherId> customerVoucherIdObjectProvider;

    @Autowired
    public CustomerVoucherHelper(@Qualifier("domainService")
                                 IDomainService<Voucher> domainService,
                                 Voucher mockVoucher,
                                 ObjectProvider<UserVoucher> customerVoucherObjectProvider,
                                 ObjectProvider<UserVoucherId> customerVoucherIdObjectProvider) {
        this.domainService = domainService;
        this.mockVoucher = mockVoucher;
        this.customerVoucherObjectProvider = customerVoucherObjectProvider;
        this.customerVoucherIdObjectProvider = customerVoucherIdObjectProvider;
    }

    public UserVoucher adapt(Voucher voucher, Customer customer) {
        Long voucherId = voucher.getId();
        Optional<Voucher> voucherOptional = domainService.findById(voucherId, mockVoucher);
        Voucher voucherToSave = voucherOptional.orElseThrow(NotFoundException::new);

        UserVoucher customerVoucher = customerVoucherObjectProvider.getObject();
        UserVoucherId customerVoucherId = customerVoucherIdObjectProvider.getObject();

        return customerVoucher.adapt(customerVoucherId, customer, voucherToSave);
    }

    public UserVoucher provideNewObject() {
        return customerVoucherObjectProvider.getObject();
    }
}
