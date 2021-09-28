package com.watashi.bookstore.entity.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class UserVoucherId implements Serializable {
    @Column(name = "use_cst_id", nullable = false)
    protected Long customerId;

    @Column(name = "use_vch_id", nullable = false)
    protected Long voucherId;

    @Column(name = "use_registered_at")
    private LocalDateTime date;

}
