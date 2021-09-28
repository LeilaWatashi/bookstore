package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.shop.Sale;
import com.watashi.bookstore.entity.shop.SaleAddress;
import com.watashi.bookstore.entity.shop.SaleAddressId;
import com.watashi.bookstore.entity.user.Address;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaleAddressHelper {

    private final IDomainService<Address> addressDomainService;
    private final Address mockAddress;

    private ObjectProvider<SaleAddress> saleAddressProvider;
    private ObjectProvider<SaleAddressId> saleAddressIdProvider;

    @Autowired
    public SaleAddressHelper(@Qualifier("domainService")
                             IDomainService<Address> addressDomainService,
                             Address mockAddress,
                             ObjectProvider<SaleAddress> saleAddressProvider,
                             ObjectProvider<SaleAddressId> saleAddressIdProvider) {
        this.addressDomainService = addressDomainService;
        this.mockAddress = mockAddress;
        this.saleAddressProvider = saleAddressProvider;
        this.saleAddressIdProvider = saleAddressIdProvider;
    }

    public SaleAddress adapt(Address address, Sale sale) {
        Long addressId = address.getId();
        Optional<Address> addressOptional = addressDomainService.findById(addressId, mockAddress);
        Address addressToSave = addressOptional.orElseThrow(NotFoundException::new);

        SaleAddress saleAddress = saleAddressProvider.getObject();
        SaleAddressId saleCreditCardId = saleAddressIdProvider.getObject();

        return saleAddress.adapt(saleCreditCardId, sale, addressToSave);
    }
}
