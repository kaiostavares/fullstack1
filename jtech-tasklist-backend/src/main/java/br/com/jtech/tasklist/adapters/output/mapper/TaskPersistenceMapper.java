package br.com.jtech.tasklist.adapters.output.mapper;

import br.com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import br.com.jtech.tasklist.application.core.domains.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskPersistenceMapper extends BasePersistenceMapper<TaskEntity, Task> {
    
    @Override
    @Mapping(target = "id", expression = "java(domain.getId() != null ? java.util.UUID.fromString(domain.getId()) : null)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    TaskEntity toEntity(Task domain);
    
    @Override
    @Mapping(target = "id", expression = "java(entity.getId() != null ? entity.getId().toString() : null)")
    Task toDomain(TaskEntity entity);
}
