package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DAO<T extends Entity> extends JpaRepository<T, Long> {
}
