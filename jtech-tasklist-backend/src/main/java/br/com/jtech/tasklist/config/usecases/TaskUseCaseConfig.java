package br.com.jtech.tasklist.config.usecases;

import br.com.jtech.tasklist.adapters.output.persistence.repositories.adapters.TaskRepositoryAdapter;
import br.com.jtech.tasklist.application.core.usecases.CreateTaskUseCaseImpl;
import br.com.jtech.tasklist.application.core.usecases.DeleteTaskUseCaseImpl;
import br.com.jtech.tasklist.application.core.usecases.FindAllTasksUseCaseImpl;
import br.com.jtech.tasklist.application.core.usecases.FindTaskByIdUseCaseImpl;
import br.com.jtech.tasklist.application.core.usecases.UpdateTaskUseCaseImpl;
import br.com.jtech.tasklist.application.dto.task.CreateTaskCommand;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.dto.task.UpdateTaskCommand;
import br.com.jtech.tasklist.application.ports.input.common.CreateEntityUseCase;
import br.com.jtech.tasklist.application.ports.input.common.DeleteEntityUseCase;
import br.com.jtech.tasklist.application.ports.input.common.FindAllEntitiesUseCase;
import br.com.jtech.tasklist.application.ports.input.common.FindEntityByIdUseCase;
import br.com.jtech.tasklist.application.ports.input.common.UpdateEntityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TaskUseCaseConfig {

    private final TaskRepositoryAdapter taskRepositoryAdapter;

    @Bean
    public CreateEntityUseCase<CreateTaskCommand, TaskOutput> createTaskUseCase(){
        return new CreateTaskUseCaseImpl(taskRepositoryAdapter);
    }

    @Bean
    public UpdateEntityUseCase<UpdateTaskCommand, TaskOutput> updateTaskUseCase(){
        return new UpdateTaskUseCaseImpl(taskRepositoryAdapter);
    }

    @Bean
    public FindEntityByIdUseCase<TaskOutput> findEntityByIdUseCase(){
        return new FindTaskByIdUseCaseImpl(taskRepositoryAdapter);
    }

    @Bean
    public FindAllEntitiesUseCase<TaskOutput> findAllEntitiesUseCase(){
        return new FindAllTasksUseCaseImpl(taskRepositoryAdapter);
    }

    @Bean
    public DeleteEntityUseCase deleteEntityUseCase(){
        return new DeleteTaskUseCaseImpl(taskRepositoryAdapter);
    }
}

