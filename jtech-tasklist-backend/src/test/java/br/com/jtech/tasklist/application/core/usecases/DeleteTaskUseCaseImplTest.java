package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)DeleteTaskUseCaseImplTest.java
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
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceNotFoundException;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTaskUseCase Tests")
class DeleteTaskUseCaseImplTest {

    @Mock
    private TaskPersistenceGateway persistenceGateway;

    private DeleteTaskUseCaseImpl deleteTaskUseCase;

    @BeforeEach
    void setUp() {
        deleteTaskUseCase = new DeleteTaskUseCaseImpl(persistenceGateway);
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440000";
        Task existingTask = new Task(taskId, "Task Name", "Description", TaskStatus.PENDING);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(existingTask));
        doNothing().when(persistenceGateway).delete(taskId);

        // When
        deleteTaskUseCase.execute(taskId);

        // Then
        verify(persistenceGateway).findById(taskId);
        verify(persistenceGateway).delete(taskId);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655449999";

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> deleteTaskUseCase.execute(taskId))
                .isInstanceOf(DomainResourceNotFoundException.class)
                .hasMessage("error.task.not_found");

        verify(persistenceGateway).findById(taskId);
        verify(persistenceGateway, never()).delete(anyString());
    }

    @Test
    @DisplayName("Should verify task exists before deleting")
    void shouldVerifyTaskExistsBeforeDeleting() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440001";
        Task existingTask = new Task(taskId, "Task Name", "Description", TaskStatus.PENDING);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(existingTask));

        // When
        deleteTaskUseCase.execute(taskId);

        // Then
        verify(persistenceGateway, times(1)).findById(taskId);
        verify(persistenceGateway, times(1)).delete(taskId);
    }
}
