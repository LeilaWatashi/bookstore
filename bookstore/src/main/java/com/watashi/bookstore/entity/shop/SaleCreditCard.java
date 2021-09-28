package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.AssociativeDomainEntity;
import com.watashi.bookstore.entity.user.CreditCard;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString

@EqualsAndHashCode(callSuper = false)

@Getter
@Setter

@Entity
@Table(name = "sales_credit_cards")
public class SaleCreditCard extends AssociativeDomainEntity {

    @EmbeddedId
    private SaleCreditCardId id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("creditCardId")
    @JoinColumn(name = "scc_crd_id", referencedColumnName = "id", nullable = false)
    private CreditCard creditCard;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("saleId")
    @JoinColumn(name = "scc_sls_id", referencedColumnName = "id", nullable = false)
    private Sale sale;

    @Basic
    @Column(name = "scc_value")
    private Double value;

    public SaleCreditCard adapt(SaleCreditCardId saleCreditCardId, Sale sale, CreditCardValue creditCardValue) {
        Long creditCardId = creditCardValue.getCreditCard().getId();
        saleCreditCardId.setCreditCardId(creditCardId);

        this.setId(saleCreditCardId);
        this.setCreditCard(creditCardValue.getCreditCard());
        this.setValue(creditCardValue.getValue());
        this.setSale(sale);
        return this;
    }

}
