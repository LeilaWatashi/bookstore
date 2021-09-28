package com.watashi.bookstore.entity.user;

import com.watashi.bookstore.entity.AssociativeDomainEntity;
import com.watashi.bookstore.entity.shop.Voucher;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "vouchers")
public class UserVoucher extends AssociativeDomainEntity {

    @EmbeddedId
    private UserVoucherId id;

    @Column(name = "cvh_used", nullable = false)
    protected Boolean used;

    @MapsId("voucherId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cvh_vch_id", referencedColumnName = "id", nullable = false)
    private Voucher voucher;

    @MapsId("customerId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cvh_cst_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    public UserVoucher adapt(UserVoucherId userVoucherId,
                             Customer customer,
                             Voucher voucher) {
        Long voucherId = voucher.getId();
        Long customerId = customer.getId();

        userVoucherId.setVoucherId(voucherId);
        userVoucherId.setCustomerId(customerId);
        userVoucherId.setDate(LocalDateTime.now());

        this.setId(userVoucherId);
        this.setUsed(false);
        this.setVoucher(voucher);
        this.setCustomer(customer);

        return this;
    }
}
