package br.com.jtech.tasklist.application.core.usecases;

/*
 *  @(#)FindAllTasksUseCaseImplTest.java
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
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.ports.output.TaskPersistenceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindAllTasksUseCase Tests")
class FindAllTasksUseCaseImplTest {

    @Mock
    private TaskPersistenceGateway persistenceGateway;

    private FindAllTasksUseCaseImpl findAllTasksUseCase;

    @BeforeEach
    void setUp() {
        findAllTasksUseCase = new FindAllTasksUseCaseImpl(persistenceGateway);
    }

    @Test
    @DisplayName("Should find all tasks with default pagination successfully")
    void shouldFindAllTasksWithDefaultPaginationSuccessfully() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        OffsetDateTime now = OffsetDateTime.now();
        
        Task task1 = new Task("550e8400-e29b-41d4-a716-446655440001", "Task 1", "Description 1", TaskStatus.PENDING, now);
        Task task2 = new Task("550e8400-e29b-41d4-a716-446655440002", "Task 2", "Description 2", TaskStatus.IN_PROGRESS, now);
        Task task3 = new Task("550e8400-e29b-41d4-a716-446655440003", "Task 3", "Description 3", TaskStatus.COMPLETED, now);

        Page<Task> tasksPage = new PageImpl<>(List.of(task1, task2, task3), pageable, 3);

        when(persistenceGateway.findAll(pageable)).thenReturn(tasksPage);

        // When
        Page<TaskOutput> result = findAllTasksUseCase.execute(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(20);

        assertThat(result.getContent().get(0).getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
        assertThat(result.getContent().get(0).getName()).isEqualTo("Task 1");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Task 2");
        assertThat(result.getContent().get(2).getName()).isEqualTo("Task 3");

        verify(persistenceGateway).findAll(pageable);
    }

    @Test
    @DisplayName("Should find all tasks with custom page size")
    void shouldFindAllTasksWithCustomPageSize() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        OffsetDateTime now = OffsetDateTime.now();
        
        Task task1 = new Task("550e8400-e29b-41d4-a716-446655440001", "Task 1", "Description 1", TaskStatus.PENDING, now);
        Task task2 = new Task("550e8400-e29b-41d4-a716-446655440002", "Task 2", "Description 2", TaskStatus.PENDING, now);

        Page<Task> tasksPage = new PageImpl<>(List.of(task1, task2), pageable, 2);

        when(persistenceGateway.findAll(pageable)).thenReturn(tasksPage);

        // When
        Page<TaskOutput> result = findAllTasksUseCase.execute(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(2);

        verify(persistenceGateway).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no tasks found")
    void shouldReturnEmptyPageWhenNoTasksFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(persistenceGateway.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<TaskOutput> result = findAllTasksUseCase.execute(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);

        verify(persistenceGateway).findAll(pageable);
    }

    @Test
    @DisplayName("Should find tasks on second page successfully")
    void shouldFindTasksOnSecondPageSuccessfully() {
        // Given
        Pageable pageable = PageRequest.of(1, 2);
        OffsetDateTime now = OffsetDateTime.now();
        
        Task task3 = new Task("550e8400-e29b-41d4-a716-446655440003", "Task 3", "Description 3", TaskStatus.COMPLETED, now);
        Task task4 = new Task("550e8400-e29b-41d4-a716-446655440004", "Task 4", "Description 4", TaskStatus.PENDING, now);

        Page<Task> tasksPage = new PageImpl<>(List.of(task3, task4), pageable, 4);

        when(persistenceGateway.findAll(pageable)).thenReturn(tasksPage);

        // When
        Page<TaskOutput> result = findAllTasksUseCase.execute(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440003");
        assertThat(result.getContent().get(1).getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440004");

        verify(persistenceGateway).findAll(pageable);
    }

    @Test
    @DisplayName("Should map all task attributes correctly")
    void shouldMapAllTaskAttributesCorrectly() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        OffsetDateTime now = OffsetDateTime.now();
        
        Task task = new Task("550e8400-e29b-41d4-a716-446655440000", "Test Task", "Test Description", TaskStatus.IN_PROGRESS, now);

        Page<Task> tasksPage = new PageImpl<>(List.of(task), pageable, 1);

        when(persistenceGateway.findAll(pageable)).thenReturn(tasksPage);

        // When
        Page<TaskOutput> result = findAllTasksUseCase.execute(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        TaskOutput taskOutput = result.getContent().get(0);
        
        assertThat(taskOutput.getId()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        assertThat(taskOutput.getName()).isEqualTo("Test Task");
        assertThat(taskOutput.getDescription()).isEqualTo("Test Description");
        assertThat(taskOutput.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(taskOutput.getCreatedAt()).isNotNull();

        verify(persistenceGateway).findAll(pageable);
    }

    @Test
    @DisplayName("Should call persistence gateway with correct pageable")
    void shouldCallPersistenceGatewayWithCorrectPageable() {
        // Given
        Pageable pageable = PageRequest.of(2, 15);
        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(persistenceGateway.findAll(pageable)).thenReturn(emptyPage);

        // When
        findAllTasksUseCase.execute(pageable);

        // Then
        verify(persistenceGateway).findAll(pageable);
        verifyNoMoreInteractions(persistenceGateway);
    }
}
