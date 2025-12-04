package br.com.jtech.tasklist.adapters.output.mapper;

import br.com.jtech.tasklist.adapters.output.persistence.entities.AbstractEntity;
import br.com.jtech.tasklist.application.core.domains.AbstractDomainEntity;

public interface BasePersistenceMapper<E extends AbstractEntity, D extends AbstractDomainEntity>{
    E toEntity(D domain);
    D toDomain(E entity);
}
