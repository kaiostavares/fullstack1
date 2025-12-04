package br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa;

import br.com.jtech.tasklist.adapters.output.persistence.entities.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseJpaRepository<T extends AbstractEntity> extends JpaRepository<T, UUID> {
    Optional<T> findByIdAndDeletedFalse(UUID id);

    default void softDelete(T entity) {
        entity.softDelete();
        save(entity);
    }

    default void softDeleteById(UUID id) {
        Optional<T> entity = findByIdAndDeletedFalse(id);
        entity.ifPresent(this::softDelete);
    }
}
