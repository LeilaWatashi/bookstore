package com.watashi.bookstore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class AssociativeDomainEntity extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

}

