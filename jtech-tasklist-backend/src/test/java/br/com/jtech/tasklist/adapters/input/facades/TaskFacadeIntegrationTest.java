package br.com.jtech.tasklist.adapters.input.facades;

import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceAlreadyExists;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceNotFoundException;
import br.com.jtech.tasklist.application.dto.task.CreateTaskCommand;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.dto.task.UpdateTaskCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes de integração para TaskFacade.
 * Valida o funcionamento completo do fluxo incluindo persistência no banco de dados.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TaskFacade Integration Tests")
class TaskFacadeIntegrationTest {

    @Autowired
    private TaskFacade taskFacade;

    private String createdTaskId;

    @Test
    @Order(1)
    @DisplayName("Should create task successfully and persist in database")
    void shouldCreateTaskSuccessfully() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "Integration Test Task",
                "Testing database persistence",
                TaskStatus.PENDING
        );

        // When
        TaskOutput result = taskFacade.createTask(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Integration Test Task");
        assertThat(result.getDescription()).isEqualTo("Testing database persistence");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.PENDING);

        // Store ID for next tests
        createdTaskId = result.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Should find task by ID from database")
    void shouldFindTaskById() {
        // Given
        CreateTaskCommand createCommand = new CreateTaskCommand(
                "Find Test Task",
                "Task to be found",
                TaskStatus.IN_PROGRESS
        );
        TaskOutput created = taskFacade.createTask(createCommand);

        // When
        TaskOutput found = taskFacade.findTaskById(created.getId());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getName()).isEqualTo("Find Test Task");
        assertThat(found.getDescription()).isEqualTo("Task to be found");
        assertThat(found.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @Order(3)
    @DisplayName("Should update task and persist changes in database")
    void shouldUpdateTaskSuccessfully() {
        // Given
        CreateTaskCommand createCommand = new CreateTaskCommand(
                "Update Test Task",
                "Original description",
                TaskStatus.PENDING
        );
        TaskOutput created = taskFacade.createTask(createCommand);

        UpdateTaskCommand updateCommand = new UpdateTaskCommand(
                created.getId(),
                "Updated Task Name",
                "Updated description",
                TaskStatus.COMPLETED
        );

        // When
        TaskOutput updated = taskFacade.updateTask(updateCommand);

        // Then
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getName()).isEqualTo("Updated Task Name");
        assertThat(updated.getDescription()).isEqualTo("Updated description");
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.COMPLETED);

        // Verify persistence
        TaskOutput found = taskFacade.findTaskById(created.getId());
        assertThat(found.getName()).isEqualTo("Updated Task Name");
        assertThat(found.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @Order(4)
    @DisplayName("Should soft delete task in database")
    void shouldDeleteTaskSuccessfully() {
        // Given
        CreateTaskCommand createCommand = new CreateTaskCommand(
                "Delete Test Task",
                "Task to be deleted",
                TaskStatus.PENDING
        );
        TaskOutput created = taskFacade.createTask(createCommand);

        // When
        taskFacade.deleteTask(created.getId());

        // Then - Task should not be found after soft delete
        assertThatThrownBy(() -> taskFacade.findTaskById(created.getId()))
                .isInstanceOf(DomainResourceNotFoundException.class);
    }

    @Test
    @Order(5)
    @DisplayName("Should throw exception when creating task with duplicate name")
    void shouldThrowExceptionForDuplicateName() {
        // Given
        String uniqueName = "Duplicate Task " + System.currentTimeMillis();
        CreateTaskCommand command1 = new CreateTaskCommand(
                uniqueName,
                "First task",
                TaskStatus.PENDING
        );
        taskFacade.createTask(command1);

        CreateTaskCommand command2 = new CreateTaskCommand(
                uniqueName,
                "Second task with same name",
                TaskStatus.PENDING
        );

        // When/Then
        assertThatThrownBy(() -> taskFacade.createTask(command2))
                .isInstanceOf(DomainResourceAlreadyExists.class)
                .hasMessage("error.task.name.already_exists");
    }

    @Test
    @Order(6)
    @DisplayName("Should throw exception when finding non-existent task")
    void shouldThrowExceptionForNonExistentTask() {
        // Given
        String nonExistentId = "550e8400-e29b-41d4-a716-446655440099";

        // When/Then
        assertThatThrownBy(() -> taskFacade.findTaskById(nonExistentId))
                .isInstanceOf(DomainResourceNotFoundException.class);
    }

    @Test
    @Order(7)
    @DisplayName("Should throw exception when updating non-existent task")
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440098",
                "Non Existent",
                "This task does not exist",
                TaskStatus.PENDING
        );

        // When/Then
        assertThatThrownBy(() -> taskFacade.updateTask(command))
                .isInstanceOf(DomainResourceNotFoundException.class);
    }

    @Test
    @Order(8)
    @DisplayName("Should handle case-insensitive duplicate name check")
    void shouldHandleCaseInsensitiveDuplicateNames() {
        // Given
        String uniqueName = "Case Test Task " + System.currentTimeMillis();
        CreateTaskCommand command1 = new CreateTaskCommand(
                uniqueName,
                "First task",
                TaskStatus.PENDING
        );
        taskFacade.createTask(command1);

        CreateTaskCommand command2 = new CreateTaskCommand(
                uniqueName.toUpperCase(),
                "Second task with same name in upper case",
                TaskStatus.PENDING
        );

        // When/Then
        assertThatThrownBy(() -> taskFacade.createTask(command2))
                .isInstanceOf(DomainResourceAlreadyExists.class)
                .hasMessage("error.task.name.already_exists");
    }

    @Test
    @Order(9)
    @DisplayName("Should update task status from PENDING to COMPLETED")
    void shouldUpdateTaskStatus() {
        // Given
        CreateTaskCommand createCommand = new CreateTaskCommand(
                "Status Test Task",
                "Testing status change",
                TaskStatus.PENDING
        );
        TaskOutput created = taskFacade.createTask(createCommand);

        UpdateTaskCommand updateCommand = new UpdateTaskCommand(
                created.getId(),
                "Status Test Task",
                "Testing status change",
                TaskStatus.COMPLETED
        );

        // When
        TaskOutput updated = taskFacade.updateTask(updateCommand);

        // Then
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(created.getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @Order(10)
    @DisplayName("Should create multiple tasks with different statuses")
    void shouldCreateMultipleTasksWithDifferentStatuses() {
        // Given & When
        TaskOutput pending = taskFacade.createTask(new CreateTaskCommand(
                "Pending Task",
                "Task in pending status",
                TaskStatus.PENDING
        ));

        TaskOutput inProgress = taskFacade.createTask(new CreateTaskCommand(
                "In Progress Task",
                "Task in progress status",
                TaskStatus.IN_PROGRESS
        ));

        TaskOutput completed = taskFacade.createTask(new CreateTaskCommand(
                "Completed Task",
                "Task in completed status",
                TaskStatus.COMPLETED
        ));

        // Then
        assertThat(pending.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(inProgress.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(completed.getStatus()).isEqualTo(TaskStatus.COMPLETED);

        // Verify all can be found
        assertThat(taskFacade.findTaskById(pending.getId())).isNotNull();
        assertThat(taskFacade.findTaskById(inProgress.getId())).isNotNull();
        assertThat(taskFacade.findTaskById(completed.getId())).isNotNull();
    }
}
