package br.com.jtech.tasklist.adapters.output.persistence;

import br.com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa.TaskJpaRepository;
import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para verificar soft delete e campos de auditoria.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Task Persistence Integration Tests - Auditing and Soft Delete")
class TaskPersistenceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Test
    @DisplayName("Should automatically populate createdAt and updatedAt on insert")
    void shouldPopulateAuditFieldsOnInsert() {
        // Given
        TaskEntity task = new TaskEntity();
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);

        // When
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        entityManager.clear();

        // Then
        TaskEntity foundTask = taskJpaRepository.findById(savedTask.getId()).orElseThrow();
        
        assertThat(foundTask.getCreatedAt()).isNotNull();
        assertThat(foundTask.getUpdatedAt()).isNotNull();
        assertThat(foundTask.getCreatedAt()).isEqualTo(foundTask.getUpdatedAt());
        assertThat(foundTask.getDeleted()).isFalse();
    }

    @Test
    @DisplayName("Should automatically update updatedAt on modification")
    void shouldUpdateUpdatedAtOnModification() throws InterruptedException {
        // Given
        TaskEntity task = new TaskEntity();
        task.setName("Original Task");
        task.setDescription("Original Description");
        task.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        
        // Small delay to ensure updatedAt changes
        Thread.sleep(100);
        
        // When - Update the task
        savedTask.setName("Updated Task");
        savedTask.setStatus(TaskStatus.IN_PROGRESS);
        TaskEntity updatedTask = taskJpaRepository.save(savedTask);
        entityManager.flush();
        entityManager.clear();

        // Then
        TaskEntity foundTask = taskJpaRepository.findById(updatedTask.getId()).orElseThrow();
        
        assertThat(foundTask.getCreatedAt()).isNotNull();
        assertThat(foundTask.getUpdatedAt()).isNotNull();
        assertThat(foundTask.getUpdatedAt()).isAfter(foundTask.getCreatedAt());
    }

    @Test
    @DisplayName("Should perform soft delete correctly")
    void shouldPerformSoftDeleteCorrectly() {
        // Given
        TaskEntity task = new TaskEntity();
        task.setName("Task To Delete");
        task.setDescription("This task will be soft deleted");
        task.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        
        assertThat(savedTask.getDeleted()).isFalse();
        
        // When - Soft delete
        savedTask.softDelete();
        TaskEntity deletedTask = taskJpaRepository.save(savedTask);
        entityManager.flush();
        entityManager.clear();

        // Then
        TaskEntity foundTask = taskJpaRepository.findById(deletedTask.getId()).orElseThrow();
        
        assertThat(foundTask.getDeleted()).isTrue();
        assertThat(foundTask.getId()).isEqualTo(savedTask.getId());
        assertThat(foundTask.getName()).isEqualTo("Task To Delete");
    }

    @Test
    @DisplayName("Should not find soft deleted tasks with findByNameAndDeletedFalse")
    void shouldNotFindSoftDeletedTasks() {
        // Given
        TaskEntity task = new TaskEntity();
        task.setName("Deletable Task");
        task.setDescription("This task will be soft deleted and not found");
        task.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        
        // Verify task can be found before deletion
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("Deletable Task")).isPresent();
        
        // When - Soft delete
        savedTask.softDelete();
        taskJpaRepository.save(savedTask);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("Deletable Task")).isEmpty();
    }

    @Test
    @DisplayName("Should allow creating task with same name after soft delete")
    void shouldAllowSameNameAfterSoftDelete() {
        // Given
        TaskEntity task1 = new TaskEntity();
        task1.setName("Reusable Name");
        task1.setDescription("First task");
        task1.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask1 = taskJpaRepository.save(task1);
        entityManager.flush();
        
        // Soft delete first task
        savedTask1.softDelete();
        taskJpaRepository.save(savedTask1);
        entityManager.flush();
        
        // When - Create new task with same name
        TaskEntity task2 = new TaskEntity();
        task2.setName("Reusable Name");
        task2.setDescription("Second task");
        task2.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask2 = taskJpaRepository.save(task2);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedTask2.getId()).isNotEqualTo(savedTask1.getId());
        assertThat(savedTask2.getName()).isEqualTo("Reusable Name");
        assertThat(savedTask2.getDeleted()).isFalse();
        
        // Verify only the active task is found
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("Reusable Name"))
                .isPresent()
                .get()
                .extracting(TaskEntity::getId)
                .isEqualTo(savedTask2.getId());
    }

    @Test
    @DisplayName("Should verify case-insensitive name search works correctly")
    void shouldFindTaskCaseInsensitive() {
        // Given
        TaskEntity task = new TaskEntity();
        task.setName("CaseSensitive Task");
        task.setDescription("Testing case sensitivity");
        task.setStatus(TaskStatus.PENDING);
        
        taskJpaRepository.save(task);
        entityManager.flush();
        entityManager.clear();

        // When & Then - Different cases should find the same task
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("CaseSensitive Task")).isPresent();
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("casesensitive task")).isPresent();
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("CASESENSITIVE TASK")).isPresent();
        assertThat(taskJpaRepository.findByNameAndDeletedFalse("CaSeSenSiTiVe TaSk")).isPresent();
    }
}
