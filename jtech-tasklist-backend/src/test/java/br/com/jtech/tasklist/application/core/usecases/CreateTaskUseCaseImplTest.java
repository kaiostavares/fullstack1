package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)CreateTaskUseCaseImplTest.java
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
import br.com.jtech.tasklist.application.dto.task.CreateTaskCommand;
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
@DisplayName("CreateTaskUseCase Tests")
class CreateTaskUseCaseImplTest {

    @Mock
    private TaskPersistenceGateway persistenceGateway;

    private CreateTaskUseCaseImpl createTaskUseCase;

    @BeforeEach
    void setUp() {
        createTaskUseCase = new CreateTaskUseCaseImpl(persistenceGateway);
    }

    @Test
    @DisplayName("Should create task successfully when name does not exist")
    void shouldCreateTaskSuccessfully() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "New Task",
                "Task Description",
                TaskStatus.PENDING
        );

        Task savedTask = new Task("550e8400-e29b-41d4-a716-446655440000", "New Task", "Task Description", TaskStatus.PENDING);

        when(persistenceGateway.findByName(anyString())).thenReturn(Optional.empty());
        when(persistenceGateway.save(any(Task.class))).thenReturn(savedTask);

        // When
        TaskOutput result = createTaskUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        assertThat(result.getName()).isEqualTo("New Task");
        assertThat(result.getDescription()).isEqualTo("Task Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.PENDING);

        verify(persistenceGateway).findByName("new task");
        verify(persistenceGateway).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when task name already exists")
    void shouldThrowExceptionWhenTaskNameAlreadyExists() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "Existing Task",
                "Task Description",
                TaskStatus.PENDING
        );

        Task existingTask = new Task("550e8400-e29b-41d4-a716-446655440001", "Existing Task", "Description", TaskStatus.PENDING);

        when(persistenceGateway.findByName(anyString())).thenReturn(Optional.of(existingTask));

        // When/Then
        assertThatThrownBy(() -> createTaskUseCase.execute(command))
                .isInstanceOf(DomainResourceAlreadyExists.class)
                .hasMessage("error.task.name.already_exists");

        verify(persistenceGateway).findByName("existing task");
        verify(persistenceGateway, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should convert task name to lowercase when checking for duplicates")
    void shouldConvertNameToLowercaseWhenChecking() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "New TASK",
                "Task Description",
                TaskStatus.PENDING
        );

        Task savedTask = new Task("550e8400-e29b-41d4-a716-446655440002", "New TASK", "Task Description", TaskStatus.PENDING);

        when(persistenceGateway.findByName(anyString())).thenReturn(Optional.empty());
        when(persistenceGateway.save(any(Task.class))).thenReturn(savedTask);

        // When
        createTaskUseCase.execute(command);

        // Then
        verify(persistenceGateway).findByName("new task");
    }
}
