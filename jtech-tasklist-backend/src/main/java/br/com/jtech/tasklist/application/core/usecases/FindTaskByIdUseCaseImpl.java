package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)FindTaskByIdUseCaseImpl.java
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
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceNotFoundException;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.ports.input.common.FindEntityByIdUseCase;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;

public record FindTaskByIdUseCaseImpl(
        TaskPersistenceGateway persistenteGateway
) implements FindEntityByIdUseCase<TaskOutput> {

    @Override
    public TaskOutput execute(String entityId) {
        var task = persistenteGateway.findById(entityId)
                .orElseThrow(() -> new DomainResourceNotFoundException("error.task.not_found"));
        
        return new TaskOutput(task);
    }
}
