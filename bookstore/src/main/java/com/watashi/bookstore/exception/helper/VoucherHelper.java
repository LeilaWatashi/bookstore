package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.shop.Voucher;
import com.watashi.bookstore.entity.shop.VoucherType;
import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.UserVoucher;
import com.watashi.bookstore.entity.user.UserVoucherId;
import com.watashi.bookstore.service.IAssociativeDomainService;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VoucherHelper {

    private final IDomainService<Voucher> voucherDomainService;
    private final IAssociativeDomainService<UserVoucher> customerVoucherAssociativeDomainService;

    private ObjectProvider<Voucher> voucherProvider;
    private ObjectProvider<UserVoucher> customerVoucherProvider;
    private ObjectProvider<UserVoucherId> customerVoucherIdProvider;

    @Autowired
    public VoucherHelper(@Qualifier("domainService")
                         IDomainService<Voucher> voucherDomainService,
                         @Qualifier("associativeDomainService")
                         IAssociativeDomainService<UserVoucher> customerVoucherAssociativeDomainService,
                         ObjectProvider<Voucher> voucherProvider,
                         ObjectProvider<UserVoucher> userVoucherProvider,
                         ObjectProvider<UserVoucherId> userVoucherIdProvider) {
        this.voucherDomainService = voucherDomainService;
        this.customerVoucherAssociativeDomainService = customerVoucherAssociativeDomainService;
        this.voucherProvider = voucherProvider;
        this.customerVoucherProvider = userVoucherProvider;
        this.customerVoucherIdProvider = userVoucherIdProvider;
    }


    public Voucher adapt(Double value, VoucherType type) {
        Voucher newVoucher = voucherProvider.getObject();

        if (type.equals(VoucherType.TRADE)) {
            String name = "Cupom de " + type.getDisplayName() +
                    " R$ " + (int) Math.ceil(value);

            newVoucher.setName(name);
            newVoucher.setType(type);
            newVoucher.setValue(value);
            newVoucher.setMultiplicationFactor(1d);
        }

        return newVoucher;
    }

    public UserVoucher adapt(Customer customer, Voucher voucher) {
        UserVoucher newCustomerVoucher = customerVoucherProvider.getObject();
        UserVoucherId newCustomerVoucherId = customerVoucherIdProvider.getObject();

        newCustomerVoucherId.setCustomerId(customer.getId());
        newCustomerVoucherId.setVoucherId(voucher.getId());
        newCustomerVoucherId.setDate(LocalDateTime.now());

        newCustomerVoucher.setId(newCustomerVoucherId);

        newCustomerVoucher.setUsed(false);
        newCustomerVoucher.setCustomer(customer);
        newCustomerVoucher.setVoucher(voucher);

        return newCustomerVoucher;
    }

    public Voucher adaptAndSave(Double value, VoucherType type) {
        Voucher voucher = adapt(value, type);

        return voucherDomainService.save(voucher);
    }

    public UserVoucher adaptAndSave(Customer customer, Voucher voucher) {
        UserVoucher customerVoucher = adapt(customer, voucher);

        return customerVoucherAssociativeDomainService.save(customerVoucher);
    }
}
