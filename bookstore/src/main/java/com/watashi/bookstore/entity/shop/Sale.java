package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.DomainEntity;
import com.watashi.bookstore.entity.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "sales")
public class Sale extends DomainEntity {

    @Basic
    @Column(name = "sls_identify_number")
    private String identifyNumber;

    @CreationTimestamp
    @Column(name = "sls_purchase_date")
    private LocalDateTime date;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "sls_status")
    private SalesStatusType status;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "sls_cst_id", foreignKey = @ForeignKey(name = "sls_cst_fk"))
    private Customer customer;

    @OneToMany(mappedBy = "sale", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH })
    private List<SaleItem> items;

    @OneToMany(mappedBy = "sale", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH })
    private List<SaleAddress> adresses;

    @OneToMany(mappedBy = "sale", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH })
    private List<SaleCreditCard> usedCreditCards;

    @ManyToOne
    @JoinColumn(name = "sls_vcr_id", foreignKey = @ForeignKey(name = "sls_vch_id"))
    private Voucher voucher;

    @OneToMany(mappedBy = "order")
    private List<Trade> trades;

    @OneToOne(mappedBy = "sale", cascade = CascadeType.ALL, optional = false)
    private Freight freight;

    public void putStatusToProcessing() {
        this.status = SalesStatusType.PROCESSING;
    }

    public void changeStatusToInTransit() {
        this.status = SalesStatusType.IN_TRANSIT;
    }

    public void changeStatusToDelivered() {
        this.status = SalesStatusType.DELIVERED;
    }

    public String computeStatusColorClass(String prefix) {
        StringBuilder statusColor = new StringBuilder(prefix);
        statusColor.append("-");

        if (status.equals(SalesStatusType.PROCESSING)) {
            statusColor.append("info");
        }
        if (status.equals(SalesStatusType.IN_TRANSIT)) {
            statusColor.append("warning");
        }
        if (status.equals(SalesStatusType.DELIVERED)) {
            statusColor.append("success");
        }

        return statusColor.toString();
    }

    public boolean inProcessing() {
        return status.equals(SalesStatusType.PROCESSING);
    }

    public boolean inTransit() {
        return status.equals(SalesStatusType.IN_TRANSIT);
    }

    public boolean delivered() {
        return status.equals(SalesStatusType.DELIVERED);
    }

    public boolean wasOrIsInProcessing() {
        return inProcessing() || inTransit() || delivered();
    }

    public boolean wasOrIsInTransit() {
        return inTransit() || delivered();
    }

    public Boolean hasAnyVoucherApplied() {
        return voucher != null && voucher.getId() != null
                && (voucher.getMultiplicationFactor() != null || voucher.getValue() != null);
    }

    public Double calculateTotal() {
        Double subtotal = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(.0, Double::sum);

        return hasAnyVoucherApplied()
                ? subtotal - calculateDifferenceWithVoucher(subtotal)
                : subtotal;
    }

    private Double calculateDifferenceWithVoucher(Double subtotal) {
        Double coefficient = isDiscountVoucher()
                ? voucher.getMultiplicationFactor()
                : voucher.getValue();

        return (isDiscountVoucher()
                ? subtotal * coefficient
                : Math.min(coefficient, subtotal));
    }

    public Double calculateTotalWithFreight() {
        return calculateTotalWithoutVoucher() + freight.getValue();
    }

    public Double calculateTotalWithoutVoucher() {
        return hasAnyVoucherApplied()
                ? (isDiscountVoucher()
                    ? calculateTotal() / (1 - voucher.getMultiplicationFactor())
                    : calculateTotal() - voucher.getValue())
                : calculateTotal();
    }

    public Boolean isDiscountVoucher() {
        return voucher.getType().equals(VoucherType.DISCOUNT);
    }

    public TradeType whichExchangeTypeIsEnabled() {
       return isEnableToAnyExchange() && inProcessing() ? TradeType.EXCHANGE : TradeType.DEVOLUTION;
    }

    public Boolean isEnableToAnyExchange() {
        return inProcessing() || delivered();
    }
}
