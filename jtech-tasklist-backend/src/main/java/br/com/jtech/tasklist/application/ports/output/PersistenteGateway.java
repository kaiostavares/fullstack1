package br.com.jtech.tasklist.application.ports.output;

import br.com.jtech.tasklist.application.core.domains.AbstractDomainEntity;
import br.com.jtech.tasklist.application.core.domains.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersistenteGateway<T extends AbstractDomainEntity> {
    T save(T entity);
    void delete(String id);
    void delete(T entity);
    Optional<Task> findById(String id);
    Page<T> findAll(Pageable pageable);
}
