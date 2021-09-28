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
@Table(name = "sales_products")
public class SaleItem extends AssociativeDomainEntity {

    @EmbeddedId
    private SaleItemId id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("itemId")
    @JoinColumn(name = "slp_prt_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("saleId")
    @JoinColumn(name = "slp_sls_id", referencedColumnName = "id", nullable = false)
    private Sale sale;

    @Basic
    @Column(name = "slp_quantity", nullable = false)
    private Integer quantity;

    @Basic
    @Column(name = "slp_subtotal", nullable = false)
    private Double subtotal;

    public SaleItem adapt(SaleItemId saleItemId, Sale sale, Product product,
                          Integer quantity, Double subtotal) {

        Long productId = product.getId();
        saleItemId.setItemId(productId);

        this.setId(saleItemId);
        this.setSale(sale);
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setSubtotal(subtotal);

        return this;
    }
}
