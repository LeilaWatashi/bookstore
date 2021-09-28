package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.Entity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DomainDAO<T extends Entity> extends DAO<T> {
    Optional<T> findByIdAndInactivatedFalse(Long id);

    Optional<T> findByIdAndInactivatedTrue(Long id);

    List<T> findAllByInactivatedFalse();

    List<T> findAllByInactivatedTrue();
}
