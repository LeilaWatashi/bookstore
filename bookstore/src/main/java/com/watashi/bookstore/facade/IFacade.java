package com.watashi.bookstore.facade;

import com.watashi.bookstore.entity.AssociativeDomainEntity;
import com.watashi.bookstore.entity.Entity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface IFacade<T> {

    T save(T entity);

    T saveAndFlush(T entity);

    void remove(T entity);

    Optional<T> findById(Long id, T entity);

    Optional<T> findBy(Entity targetEntity, T baseEntity);

    List<T> findAll(T entity);

    List<T> findAllBy(Entity targetEntity, T baseEntity);

    List<T> findAllValidBy(Entity targetEntity, T baseEntity);

    Optional<T> findValidByEmbeddedEntity(AssociativeDomainEntity associativeEntity,
                                          Entity entityOne, Entity entityTwo);

    Optional<T> findActivatedById(Long id, Entity entity);

    Optional<T> findInactivatedById(Long id, Entity entity);

    List<T> findAllActivatedBy(T entity);

    List<T> findAllInactivatedBy(T entity);

}
