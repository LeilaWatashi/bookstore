package com.watashi.bookstore.entity.stock;

import com.watashi.bookstore.entity.DomainEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stock_history")
public class StockHistory extends DomainEntity {
    @Column(name = "sth_quantity")
    private Integer amount;
    @CreationTimestamp
    @Column(name = "sth_date")
    private LocalDateTime date;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "sth_stc_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "sth_stc_fk"))
    private Stock stock;

    public void fillFieldsWith(Stock stock) {
        this.stock = stock;
        this.amount = stock.getLastOperationAmount();
    }

    public Integer calculateCurrentStockAmount() {
        return stock.getAmount();
    }

    public Integer calculateOldStockAmount() {
        return stock.getAmount() - amount;
    }

    public Boolean isFirstStockAmount() {
        return calculateOldStockAmount() == 0;
    }

    public Boolean isUpLastOperation() {
        return getAmount() > 0;
    }
}
