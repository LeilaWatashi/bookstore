package com.watashi.bookstore.entity.product;

import com.watashi.bookstore.entity.AlternativeDomainEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Component
@Table(name = "bookmakers")
public class Bookmaker extends AlternativeDomainEntity {
    @Column(name = "bmk_name")
    private String name;
}
