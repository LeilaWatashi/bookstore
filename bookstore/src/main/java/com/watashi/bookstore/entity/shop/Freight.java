package com.watashi.bookstore.entity.shop;


import com.watashi.bookstore.entity.DomainEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "freights")
public class Freight extends DomainEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "fgh_table")
    private FreightType freightTable;

    @Basic
    @Column(name = "fgh_value")
    private Double value;

    @Basic
    @Column(name = "fgh_estimate_in_days")
    private Integer deliveryEstimate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "fgh_sls_id")
    private Sale sale;

    public void calculate(final Double baseValue, final String zipCode) {
        int MINIMUM_DAYS = 2;
        computeFreightTableBy(zipCode);
        double coefficient = freightTable.getCoefficient();
        double weight = freightTable.getWeight();
        this.value = baseValue * (isFree() ? 0.0 : weight) * coefficient;
        deliveryEstimate = (int) Math.ceil(coefficient + MINIMUM_DAYS);
    }

    private void computeFreightTableBy(final String zipCode) {
        int DIVIDER_NUMBER = 100;
        int length = zipCode.length();
        int identifier = Integer.parseInt(zipCode.substring(length-3, length-1)) / DIVIDER_NUMBER;
        FreightType selectedFreightTable;
        if (identifier > -1 && identifier < 3) {
            selectedFreightTable = FreightType.FREE;
        } else if (identifier > 2 && identifier < 5) {
            selectedFreightTable = FreightType.SLOWER;
        } else if (identifier > 4 && identifier < 7) {
            selectedFreightTable = FreightType.FAST;
        } else if (identifier > 6 && identifier < 9) {
            selectedFreightTable = FreightType.SUPER_FAST;
        } else {
            selectedFreightTable = FreightType.NORMAL;
        }
        freightTable = selectedFreightTable;
    }

    public Boolean isFree() {
        return freightTable.equals(FreightType.FREE);
    }
}
