package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)FindTaskByIdUseCaseImplTest.java
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
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindTaskByIdUseCase Tests")
class FindTaskByIdUseCaseImplTest {

    @Mock
    private TaskPersistenceGateway persistenceGateway;

    private FindTaskByIdUseCaseImpl findTaskByIdUseCase;

    @BeforeEach
    void setUp() {
        findTaskByIdUseCase = new FindTaskByIdUseCaseImpl(persistenceGateway);
    }

    @Test
    @DisplayName("Should find task by id successfully")
    void shouldFindTaskByIdSuccessfully() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440000";
        Task existingTask = new Task(taskId, "Task Name", "Task Description", TaskStatus.PENDING);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(existingTask));

        // When
        TaskOutput result = findTaskByIdUseCase.execute(taskId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getName()).isEqualTo("Task Name");
        assertThat(result.getDescription()).isEqualTo("Task Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.PENDING);

        verify(persistenceGateway).findById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655449999";

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> findTaskByIdUseCase.execute(taskId))
                .isInstanceOf(DomainResourceNotFoundException.class)
                .hasMessage("error.task.not_found");

        verify(persistenceGateway).findById(taskId);
    }

    @Test
    @DisplayName("Should return correct task data")
    void shouldReturnCorrectTaskData() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440001";
        Task task = new Task(taskId, "My Task", "My Description", TaskStatus.COMPLETED);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(task));

        // When
        TaskOutput result = findTaskByIdUseCase.execute(taskId);

        // Then
        assertThat(result.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getName()).isEqualTo("My Task");
        assertThat(result.getDescription()).isEqualTo("My Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should call persistence gateway once")
    void shouldCallPersistenceGatewayOnce() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440002";
        Task task = new Task(taskId, "Task", "Description", TaskStatus.PENDING);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(task));

        // When
        findTaskByIdUseCase.execute(taskId);

        // Then
        verify(persistenceGateway, times(1)).findById(taskId);
    }
}
