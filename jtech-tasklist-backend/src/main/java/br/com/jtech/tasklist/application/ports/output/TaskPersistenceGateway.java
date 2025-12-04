package br.com.jtech.tasklist.application.ports.output;

import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.Optional;

public interface TaskPersistenceGateway extends PersistenteGateway<Task>{
    Optional<Task> findByName(String name);
}
