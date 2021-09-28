package com.watashi.bookstore.entity.product;

import com.watashi.bookstore.entity.AlternativeDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Component
@Table(name = "categories")
public class Category extends AlternativeDomainEntity {
    @Column(name = "ctg_name")
    private String name;

    @ManyToMany
    @JoinTable( name = "products_categories",
                joinColumns = @JoinColumn(name = "pct_ctg_id"),
                inverseJoinColumns = @JoinColumn(name = "pct_prt_id"))
    private Collection<Product> products = new HashSet<>();

}
