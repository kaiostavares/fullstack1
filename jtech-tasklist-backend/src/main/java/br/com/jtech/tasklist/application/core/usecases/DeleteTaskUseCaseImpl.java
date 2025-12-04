package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)DeleteTaskUseCaseImpl.java
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
import br.com.jtech.tasklist.application.ports.input.common.DeleteEntityUseCase;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import org.springframework.validation.annotation.Validated;


@Validated
public record DeleteTaskUseCaseImpl(
        TaskPersistenceGateway persistenteGateway
) implements DeleteEntityUseCase {

    @Override
    public void execute(String entityId) {
        persistenteGateway.findById(entityId)
                .orElseThrow(() -> new DomainResourceNotFoundException("error.task.not_found"));
        
        persistenteGateway.delete(entityId);
    }
}
