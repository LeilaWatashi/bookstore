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
public class SaleCreditCardId implements Serializable {

    @Column(name = "scc_sls_id")
    private Long saleId;

    @Column(name = "scc_crd_id")
    private Long creditCardId;
}
