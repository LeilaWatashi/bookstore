package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.AssociativeDomainEntity;
import com.watashi.bookstore.entity.product.Product;
import lombok.*;

import javax.persistence.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trades_products")
public class TradeItem extends AssociativeDomainEntity {


    @EmbeddedId
    private TradeItemId id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("itemId")
    @JoinColumn(name = "trp_prt_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("tradeId")
    @JoinColumn(name = "trp_trd_id", referencedColumnName = "id", nullable = false)
    private Trade trade;


    @Column(name = "trp_quantity")
    private Integer quantity;


    @Column(name = "trp_reason")
    private String reason;

    public TradeItem adapt(TradeItemId tradeItemId, Trade trade,
                           Product product, Integer quantity) {

        this.setRequiredAttributes(tradeItemId, trade, product, quantity);

        return this;
    }

    public TradeItem adapt(TradeItemId tradeItemId,
                           Trade trade, Product product,
                           Integer quantity, String reason) {

        this.setRequiredAttributes(tradeItemId, trade, product, quantity);
        this.setReason(reason);

        return this;
    }

    private void setRequiredAttributes(TradeItemId tradeItemId, Trade trade,
                                       Product product, Integer quantity) {

        Long productId = product.getId();
        tradeItemId.setItemId(productId);

        this.setId(tradeItemId);
        this.setTrade(trade);
        this.setProduct(product);
        this.setQuantity(quantity);
    }

}
