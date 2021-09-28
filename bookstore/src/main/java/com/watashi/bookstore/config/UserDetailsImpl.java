package com.watashi.bookstore.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
@Getter
@Setter
public class UserDetailsImpl extends User {

    private static final long serialVersionUID = 1L;
    private com.watashi.bookstore.entity.user.User user;

    public UserDetailsImpl(
            com.watashi.bookstore.entity.user.User user,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.user = user;
    }

    public UserDetailsImpl(
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {

        super(
                username, password,
                enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked,
                authorities);
    }
}
