package com.watashi.bookstore.config;


import com.watashi.bookstore.dao.UserDAO;
import com.watashi.bookstore.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findDistinctByInactivatedFalseAndUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nenhum usuário encontrado com o username: " + username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user.getUserType() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getUserType().name()));
        }

        return new UserDetailsImpl(user,
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }

}
