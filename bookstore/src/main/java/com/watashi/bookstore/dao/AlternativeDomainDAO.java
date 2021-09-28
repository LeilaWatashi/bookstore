package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.Entity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AlternativeDomainDAO<T extends Entity> extends DAO<T> {
}
