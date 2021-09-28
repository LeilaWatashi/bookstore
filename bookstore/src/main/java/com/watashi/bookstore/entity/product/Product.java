package com.watashi.bookstore.entity.product;

import com.watashi.bookstore.entity.AlternativeDomainEntity;
import com.watashi.bookstore.entity.shop.SaleItem;
import com.watashi.bookstore.entity.stock.Stock;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Component
@Table(name = "products")
public class Product extends AlternativeDomainEntity {

    @Basic
    @Column(name = "prt_title")
    private String title;

    @Basic
    @Column(name = "prt_image_url")
    private String imageUrl;

    @Basic
    @Column(name = "prt_price")
    private Double price;

    @Basic
    @Column(name = "prt_operation")
    private String operationMode;

    @Basic
    @Column(name = "prt_characteristics")
    private String characteristics;

    @Basic
    @Column(name = "prt_height")
    private String height;

    @Basic
    @Column(name = "prt_width")
    private String width;

    @Basic
    @Column(name = "prt_weight")
    private String weight;

    @Basic
    @Column(name = "prt_depth")
    private String depth;

    @OneToOne
    @JoinColumn(name = "prt_bmk_id", foreignKey = @ForeignKey(name = "prt_bmk_fk"))
    private Bookmaker bookmaker;

    @OneToOne
    @JoinColumn(name = "prt_ctg_id", foreignKey = @ForeignKey(name = "prt_ctg_fk"))
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<SaleItem> relatedSales = new ArrayList<>();

    @OneToOne(mappedBy = "product")
    private Stock stock;

    public Boolean hasStock() {
        return stock != null;
    }

    public Boolean hasNotStock() {
        return !hasStock();
    }

    public Boolean hasOnStock() {
        return hasStock() && stock.getAmount() > 0;
    }
}
