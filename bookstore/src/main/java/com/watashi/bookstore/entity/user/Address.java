package com.watashi.bookstore.entity.user;

import com.watashi.bookstore.entity.AlternativeDomainEntity;
import com.watashi.bookstore.entity.shop.SaleAddress;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Component
@Table(name = "adresses")
public class Address extends AlternativeDomainEntity {
    @Column(name = "adr_surname")
    private String surname;
    @Column(name = "adr_cep")
    private String cep;
    @Column(name = "adr_public_place")
    private String publicPlace;
    @Column(name = "adr_number")
    private String number;
    @Column(name = "adr_complement")
    private String complement;
    @Column(name = "adr_district")
    private String district;
    @Column(name = "adr_city")
    private String city;
    @Column(name = "adr_state")
    private String state;
    @JoinColumn(name = "adr_cst_id", foreignKey = @ForeignKey(name = "adr_cst_fk"))
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;
    @Enumerated(EnumType.STRING)
    @Column(name = "adr_type")
    private AddressType type;

    @OneToMany(mappedBy = "address")
    private List<SaleAddress> relatedSales = new ArrayList<>();

    public String buildAddressShortName() {
        if (publicPlace != null && number != null) {
            StringBuilder addressShortName = new StringBuilder(publicPlace);
            addressShortName.append(", ");
            addressShortName.append(number);
            if (surname != null) {
                addressShortName.append(" - ");
                addressShortName.append(surname);
            }
            if (surname == null && cep != null) {
                addressShortName.append("(");
                addressShortName.append(cep);
                addressShortName.append(")");
            }
            return addressShortName.toString();
        }
        return null;
    }

    public String buildOwnerIfSpecifiedAndAddressType() {
        StringBuilder addressShortName = new StringBuilder();
        if (surname != null) {
            addressShortName.append(surname);
        }
        if (type != null) {
            addressShortName.append(" - ");
            addressShortName.append("Endereço de ");
            addressShortName.append(type.getDisplayName());
        }
        if (surname == null && type == null && id != null) {
            addressShortName.append("Endereço ID ");
            addressShortName.append(id);
        }
        return addressShortName.toString();
    }

    public boolean isBilling() {
        return type.equals(AddressType.BILLING);
    }

    public boolean isShipping() {
        return type.equals(AddressType.SHIPPING);
    }

    public boolean isShippingAndBilling() {
        return type.equals(AddressType.SHIPPING_AND_BILLING);
    }
}

