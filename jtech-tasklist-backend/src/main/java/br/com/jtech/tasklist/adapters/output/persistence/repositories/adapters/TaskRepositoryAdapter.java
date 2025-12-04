package br.com.jtech.tasklist.adapters.output.persistence.repositories.adapters;

import br.com.jtech.tasklist.adapters.output.mapper.BasePersistenceMapper;
import br.com.jtech.tasklist.adapters.output.mapper.TaskPersistenceMapper;
import br.com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa.BaseJpaRepository;
import br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa.TaskJpaRepository;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter extends PersistenceRepositoryAdapter<TaskEntity, Task> implements TaskPersistenceGateway {

    private final TaskJpaRepository taskJpaRepository;
    private final TaskPersistenceMapper taskMapper;



    @Override
    public Optional<Task> findByName(String name) {
        return this.taskJpaRepository.findByNameAndDeletedFalse(name)
                .map(this.taskMapper::toDomain);
    }

    @Override
    protected BasePersistenceMapper<TaskEntity, Task> getDefaultMapper() {
        return this.taskMapper;
    }

    @Override
    protected BaseJpaRepository<TaskEntity> getDefaultRepository() {
        return this.taskJpaRepository;
    }
}
