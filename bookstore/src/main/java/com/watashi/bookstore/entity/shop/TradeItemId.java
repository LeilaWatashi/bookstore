package com.watashi.bookstore.entity.shop;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TradeItemId implements Serializable {
    @Column(name = "trp_trd_id")
    private Long tradeId;
    @Column(name = "trp_prt_id")
    private Long itemId;
    @Column(name = "trp_register_date")
    private LocalDateTime registerDate;
}
