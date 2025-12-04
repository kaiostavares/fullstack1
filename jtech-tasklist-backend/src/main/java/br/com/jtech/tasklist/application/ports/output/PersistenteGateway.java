package br.com.jtech.tasklist.application.ports.output;

import br.com.jtech.tasklist.application.core.domains.AbstractDomainEntity;
import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.Optional;

public interface PersistenteGateway<T extends AbstractDomainEntity> {
    T save(T entity);
    void delete(String id);
    void delete(T entity);
    Optional<Task> findById(String id);
}
