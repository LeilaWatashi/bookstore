package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends DomainDAO<User>{
    Optional<User> findDistinctByInactivatedFalseAndUsername(String username);
}
