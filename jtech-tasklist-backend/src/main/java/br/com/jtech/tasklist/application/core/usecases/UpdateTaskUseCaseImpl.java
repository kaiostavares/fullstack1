package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)UpdateTaskUseCaseImpl.java
 *
 *  Copyright (c) J-Tech Solucoes em Informatica.
 *  All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of J-Tech.
 *  ("Confidential Information"). You shall not disclose such Confidential
 *  Information and shall use it only in accordance with the terms of the
 *  license agreement you entered into with J-Tech.
 *
 */
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceAlreadyExists;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceNotFoundException;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.dto.task.UpdateTaskCommand;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.ports.input.common.UpdateEntityUseCase;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import jakarta.validation.Valid;

public record UpdateTaskUseCaseImpl(
        TaskPersistenceGateway persistenteGateway
) implements UpdateEntityUseCase<UpdateTaskCommand, TaskOutput> {

    @Override
    public TaskOutput execute(@Valid UpdateTaskCommand command) {
        var existingTask = persistenteGateway.findById(command.id())
                .orElseThrow(() -> new DomainResourceNotFoundException("error.task.not_found"));

        verifyIfTaskNameAlreadyExistsForOtherTask(command.name(), command.id());
        
        var updatedTask = new Task(command.id(), command.name(), command.description(), command.status());
        updatedTask.setCreatedAt(existingTask.getCreatedAt());
        var savedTask = persistenteGateway.save(updatedTask);
        return new TaskOutput(savedTask);
    }

    private void verifyIfTaskNameAlreadyExistsForOtherTask(String name, String currentTaskId) {
        persistenteGateway.findByName(name)
                .ifPresent(task -> {
                    if (!task.getId().equals(currentTaskId)) {
                        throw new DomainResourceAlreadyExists("error.task.name.already_exists");
                    }
                });
    }
}
