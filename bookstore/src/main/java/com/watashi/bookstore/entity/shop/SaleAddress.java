package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.AssociativeDomainEntity;
import com.watashi.bookstore.entity.user.Address;
import lombok.*;

import javax.persistence.*;

@ToString

@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(callSuper = false)

@Getter
@Setter

@Entity
@Table(name = "sales_adresses")
public class SaleAddress extends AssociativeDomainEntity {

    @EmbeddedId
    private SaleAddressId id;

    @MapsId("addressId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ssa_adr_id", referencedColumnName = "id", nullable = false)
    private Address address;

    @MapsId("saleId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ssa_sls_id", referencedColumnName = "id", nullable = false)
    private Sale sale;

    public SaleAddress adapt(SaleAddressId saleAddressId, Sale sale, Address address) {
        Long addressId = address.getId();
        saleAddressId.setAddressId(addressId);

        this.setId(saleAddressId);
        this.setAddress(address);
        this.setSale(sale);
        return this;
    }
}
