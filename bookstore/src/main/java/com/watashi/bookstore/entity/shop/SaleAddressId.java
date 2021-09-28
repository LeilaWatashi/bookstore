package com.watashi.bookstore.entity.shop;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode

@Getter
@Setter

@Embeddable
public class SaleAddressId implements Serializable {

    @Column(name = "ssa_sls_id", nullable = false)
    protected Long saleId;

    @Column(name = "ssa_adr_id", nullable = false)
    protected Long addressId;
}
