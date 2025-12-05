/*
 *  @(#)FindAllTasksUseCaseImpl.java
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
package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.ports.input.common.FindAllEntitiesUseCase;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record FindAllTasksUseCaseImpl(
        TaskPersistenceGateway persistenceGateway
) implements FindAllEntitiesUseCase<TaskOutput> {

    @Override
    public Page<TaskOutput> execute(Pageable pageable) {
        return persistenceGateway.findAll(pageable)
                .map(TaskOutput::new);
    }
}
