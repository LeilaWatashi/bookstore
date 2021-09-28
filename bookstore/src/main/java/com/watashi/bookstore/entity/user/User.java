package com.watashi.bookstore.entity.user;

import com.watashi.bookstore.entity.AlternativeDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
@Table(name = "users")
public class User extends AlternativeDomainEntity {
    @Column(name = "usr_username", nullable = false)
    private String username;
    @Column (name = "usr_password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "usr_userType")
    private UserType userType;
    @Transient
    private String confirmPassword;
}
