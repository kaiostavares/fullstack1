package br.com.jtech.tasklist.adapters.output.persistence.repositories.adapters;

import br.com.jtech.tasklist.adapters.output.mapper.BasePersistenceMapper;
import br.com.jtech.tasklist.adapters.output.persistence.entities.AbstractEntity;
import br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa.BaseJpaRepository;
import br.com.jtech.tasklist.application.core.domains.AbstractDomainEntity;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.PersistenteGateway;

import java.util.Optional;
import java.util.UUID;

public abstract class PersistenceRepositoryAdapter<E extends AbstractEntity, D extends AbstractDomainEntity>
        implements PersistenteGateway<D> {

    protected abstract BasePersistenceMapper<E, D> getDefaultMapper();
    protected abstract BaseJpaRepository<E> getDefaultRepository();

    @Override
    public D save(D entity) {
        E e = this.getDefaultMapper().toEntity(entity);
        E savedEntity = this.getDefaultRepository().save(e);
        return this.getDefaultMapper().toDomain(savedEntity);
    }

    @Override
    public void delete(String id) {
        this.getDefaultRepository().softDeleteById(UUID.fromString(id));
    }

    @Override
    public void delete(D entity) {
        E e = this.getDefaultMapper().toEntity(entity);
        this.getDefaultRepository().softDelete(e);
    }

    @Override
    public Optional<Task> findById(String id) {
        Optional<E> entity = this.getDefaultRepository().findByIdAndDeletedFalse(UUID.fromString(id));
        if(entity.isPresent()) {
            D domain = this.getDefaultMapper().toDomain(entity.get());
            return Optional.of((Task) domain);
        } else {
            return Optional.empty();
        }
    }
}
