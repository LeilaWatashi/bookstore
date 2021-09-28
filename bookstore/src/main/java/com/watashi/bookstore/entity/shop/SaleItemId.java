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
public class SaleItemId implements Serializable {

    @Column(name = "slp_sls_id")
    private Long saleId;

    @Column(name = "slp_prt_id")
    private Long itemId;
}
