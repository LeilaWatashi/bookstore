package com.watashi.bookstore.service;

import com.watashi.bookstore.entity.DomainEntity;
import com.watashi.bookstore.entity.Entity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface IAlternativeDomainService<T extends DomainEntity> extends Service<T> {

    T save(T domainEntity);

    T saveAndFlush(T domainEntity);

    List<T> findAll(T mockEntity);

    List<T> findAllBy(Entity targetEntity, T mockBaseEntity);

    Optional<T> findById(Long id, T mockEntity);

    Optional<T> findBy(Entity targetEntity, T mockBaseEntity);

}
