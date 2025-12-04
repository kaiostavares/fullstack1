package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)UpdateTaskUseCaseImplTest.java
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
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceAlreadyExists;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceNotFoundException;
import br.com.jtech.tasklist.application.dto.task.UpdateTaskCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTaskUseCase Tests")
class UpdateTaskUseCaseImplTest {

    @Mock
    private TaskPersistenceGateway persistenceGateway;

    private UpdateTaskUseCaseImpl updateTaskUseCase;

    @BeforeEach
    void setUp() {
        updateTaskUseCase = new UpdateTaskUseCaseImpl(persistenceGateway);
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440000";
        UpdateTaskCommand command = new UpdateTaskCommand(
                taskId,
                "Updated Task",
                "Updated Description",
                TaskStatus.IN_PROGRESS
        );

        Task existingTask = new Task(taskId, "Old Task", "Old Description", TaskStatus.PENDING);
        Task updatedTask = new Task(taskId, "Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(persistenceGateway.findByName(anyString())).thenReturn(Optional.empty());
        when(persistenceGateway.save(any(Task.class))).thenReturn(updatedTask);

        // When
        TaskOutput result = updateTaskUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getName()).isEqualTo("Updated Task");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        verify(persistenceGateway).findById(taskId);
        verify(persistenceGateway).findByName("updated task");
        verify(persistenceGateway).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655449999";
        UpdateTaskCommand command = new UpdateTaskCommand(
                taskId,
                "Updated Task",
                "Updated Description",
                TaskStatus.IN_PROGRESS
        );

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> updateTaskUseCase.execute(command))
                .isInstanceOf(DomainResourceNotFoundException.class)
                .hasMessage("error.task.not_found");

        verify(persistenceGateway).findById(taskId);
        verify(persistenceGateway, never()).findByName(anyString());
        verify(persistenceGateway, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when name already exists for another task")
    void shouldThrowExceptionWhenNameExistsForAnotherTask() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440001";
        UpdateTaskCommand command = new UpdateTaskCommand(
                taskId,
                "Existing Task Name",
                "Updated Description",
                TaskStatus.IN_PROGRESS
        );

        Task existingTask = new Task(taskId, "Old Task", "Old Description", TaskStatus.PENDING);
        Task anotherTask = new Task("550e8400-e29b-41d4-a716-446655440002", "Existing Task Name", "Description", TaskStatus.PENDING);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(persistenceGateway.findByName("existing task name")).thenReturn(Optional.of(anotherTask));

        // When/Then
        assertThatThrownBy(() -> updateTaskUseCase.execute(command))
                .isInstanceOf(DomainResourceAlreadyExists.class)
                .hasMessage("error.task.name.already_exists");

        verify(persistenceGateway).findById(taskId);
        verify(persistenceGateway).findByName("existing task name");
        verify(persistenceGateway, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should allow update when name exists for same task")
    void shouldAllowUpdateWhenNameExistsForSameTask() {
        // Given
        String taskId = "550e8400-e29b-41d4-a716-446655440003";
        UpdateTaskCommand command = new UpdateTaskCommand(
                taskId,
                "Same Task Name",
                "Updated Description",
                TaskStatus.IN_PROGRESS
        );

        Task existingTask = new Task(taskId, "Same Task Name", "Old Description", TaskStatus.PENDING);
        Task updatedTask = new Task(taskId, "Same Task Name", "Updated Description", TaskStatus.IN_PROGRESS);

        when(persistenceGateway.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(persistenceGateway.findByName("same task name")).thenReturn(Optional.of(existingTask));
        when(persistenceGateway.save(any(Task.class))).thenReturn(updatedTask);

        // When
        TaskOutput result = updateTaskUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);

        verify(persistenceGateway).findById(taskId);
        verify(persistenceGateway).findByName("same task name");
        verify(persistenceGateway).save(any(Task.class));
    }
}
