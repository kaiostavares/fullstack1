package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)CreateTaskUseCaseImpl.java
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
import br.com.jtech.tasklist.application.dto.task.CreateTaskCommand;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.ports.input.common.CreateEntityUseCase;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;


@Validated
public record CreateTaskUseCaseImpl (
        TaskPersistenceGateway persistenteGateway
) implements CreateEntityUseCase<CreateTaskCommand, TaskOutput> {


    @Override
    public TaskOutput execute(@Valid CreateTaskCommand command) {
        verifyIfTaskNameAlreadyExists(command.name());
        var createdTask = persistenteGateway.save(command.toEntity());
        return new TaskOutput(createdTask);
    }

    private void verifyIfTaskNameAlreadyExists(String name) {
        persistenteGateway.findByName(name.toLowerCase())
                .ifPresent(task -> {
                    throw new DomainResourceAlreadyExists("error.task.name.already_exists");
                });
    }
}