package com.watashi.bookstore.service;

import com.watashi.bookstore.entity.DomainEntity;
import com.watashi.bookstore.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface IDomainService<T extends DomainEntity> extends Service<T>{

    T save(T domainEntity);

    T saveAndFlush(T domainEntity);

    List<T> findAll(T mockEntity);

    List<T> findAllBy(Entity targetEntity, T mockBaseEntity);

    Optional<T> findById(Long id, T mockEntity);

    Optional<T> findBy(Entity targetEntity, T mockBaseEntity);

    Optional<T> findActivatedById(Long id, Entity entity);

    Optional<T> findInactivatedById(Long id, Entity entity);

    List<T> findAllActivatedBy(T entity);

    List<T> findAllInactivatedBy(T entity);
}
