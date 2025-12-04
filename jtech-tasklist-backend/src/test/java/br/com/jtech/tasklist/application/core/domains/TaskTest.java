package br.com.jtech.tasklist.application.core.domains;

/*
 *  @(#)TaskTest.java
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
import br.com.jtech.tasklist.application.core.exceptions.DomainInvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Task Domain Tests")
class TaskTest {

    @Test
    @DisplayName("Should create a task successfully with valid data")
    void shouldCreateTaskSuccessfully() {
        // Given
        String name = "Test Task";
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;

        // When
        Task task = new Task(name, description, status);

        // Then
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(name);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should create a task with ID successfully")
    void shouldCreateTaskWithIdSuccessfully() {
        // Given
        String id = "550e8400-e29b-41d4-a716-446655440000";
        String name = "Test Task";
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;

        // When
        Task task = new Task(id, name, description, status);

        // Then
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getName()).isEqualTo(name);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        // Given
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;

        // When/Then
        assertThatThrownBy(() -> new Task(null, description, status))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.name.null_or_empty");
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Given
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;

        // When/Then
        assertThatThrownBy(() -> new Task("", description, status))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.name.null_or_empty");
    }

    @Test
    @DisplayName("Should throw exception when name is too long")
    void shouldThrowExceptionWhenNameIsTooLong() {
        // Given
        String name = "a".repeat(51);
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;

        // When/Then
        assertThatThrownBy(() -> new Task(name, description, status))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.name.too_long");
    }

    @Test
    @DisplayName("Should throw exception when description is null")
    void shouldThrowExceptionWhenDescriptionIsNull() {
        // Given
        String name = "Test Task";
        TaskStatus status = TaskStatus.PENDING;

        // When/Then
        assertThatThrownBy(() -> new Task(name, null, status))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.description.null_or_empty");
    }

    @Test
    @DisplayName("Should throw exception when description is empty")
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // Given
        String name = "Test Task";
        TaskStatus status = TaskStatus.PENDING;

        // When/Then
        assertThatThrownBy(() -> new Task(name, "", status))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.description.null_or_empty");
    }

    @Test
    @DisplayName("Should throw exception when description is too long")
    void shouldThrowExceptionWhenDescriptionIsTooLong() {
        // Given
        String name = "Test Task";
        String description = "a".repeat(501);
        TaskStatus status = TaskStatus.PENDING;

        // When/Then
        assertThatThrownBy(() -> new Task(name, description, status))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.description.too_long");
    }

    @Test
    @DisplayName("Should throw exception when status is null")
    void shouldThrowExceptionWhenStatusIsNull() {
        // Given
        String name = "Test Task";
        String description = "Test Description";

        // When/Then
        assertThatThrownBy(() -> new Task(name, description, null))
                .isInstanceOf(DomainInvalidArgumentException.class)
                .hasMessage("error.task.status.null_or_empty");
    }

    @Test
    @DisplayName("Should update task name successfully")
    void shouldUpdateTaskNameSuccessfully() {
        // Given
        Task task = new Task("Old Name", "Description", TaskStatus.PENDING);
        String newName = "New Name";

        // When
        task.setName(newName);

        // Then
        assertThat(task.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Should update task description successfully")
    void shouldUpdateTaskDescriptionSuccessfully() {
        // Given
        Task task = new Task("Name", "Old Description", TaskStatus.PENDING);
        String newDescription = "New Description";

        // When
        task.setDescription(newDescription);

        // Then
        assertThat(task.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("Should update task status successfully")
    void shouldUpdateTaskStatusSuccessfully() {
        // Given
        Task task = new Task("Name", "Description", TaskStatus.PENDING);

        // When
        task.setStatus(TaskStatus.COMPLETED);

        // Then
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }
}
