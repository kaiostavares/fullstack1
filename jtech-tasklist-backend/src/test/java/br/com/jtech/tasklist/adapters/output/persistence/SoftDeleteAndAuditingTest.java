package br.com.jtech.tasklist.adapters.output.persistence;

import br.com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa.TaskJpaRepository;
import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste simples para verificar soft delete e auditoria.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Soft Delete and Auditing Verification")
class SoftDeleteAndAuditingTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Test
    @Order(1)
    @DisplayName("✅ Verify Auditing: createdAt and updatedAt are automatically populated")
    void verifyAuditingFields() throws InterruptedException {
        // Create task
        TaskEntity task = new TaskEntity();
        task.setName("Audit Test Task");
        task.setDescription("Testing audit fields");
        task.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        
        assertThat(savedTask.getCreatedAt()).isNotNull();
        assertThat(savedTask.getUpdatedAt()).isNotNull();
        assertThat(savedTask.getDeleted()).isFalse();
        
        OffsetDateTime originalCreatedAt = savedTask.getCreatedAt();
        OffsetDateTime originalUpdatedAt = savedTask.getUpdatedAt();
        
        // Wait and update
        Thread.sleep(100);
        
        savedTask.setName("Updated Audit Test Task");
        TaskEntity updatedTask = taskJpaRepository.save(savedTask);
        entityManager.flush();
        
        assertThat(updatedTask.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(updatedTask.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @Order(2)
    @DisplayName("✅ Verify Soft Delete: deleted flag is set, record persists in DB")
    void verifySoftDelete() {
        // Create task
        TaskEntity task = new TaskEntity();
        task.setName("Task To Delete");
        task.setDescription("This will be soft deleted");
        task.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        
        assertThat(savedTask.getDeleted()).isFalse();
        
        // Soft delete
        savedTask.softDelete();
        TaskEntity deletedTask = taskJpaRepository.save(savedTask);
        entityManager.flush();
        entityManager.clear();
        
        // Verify task still exists in DB
        TaskEntity foundTask = taskJpaRepository.findById(deletedTask.getId()).orElse(null);
        
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getDeleted()).isTrue();
        assertThat(foundTask.getName()).isEqualTo("Task To Delete");
    }

    @Test
    @Order(3)
    @DisplayName("✅ Verify Query Filter: soft deleted tasks are excluded from findByNameAndDeletedFalse")
    void verifyQueryFilter() {
        // Create and save task
        TaskEntity task = new TaskEntity();
        task.setName("Filterable Task");
        task.setDescription("Testing query filter");
        task.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask = taskJpaRepository.save(task);
        entityManager.flush();
        
        // Verify task can be found
        boolean foundBeforeDelete = taskJpaRepository.findByNameAndDeletedFalse("Filterable Task").isPresent();
        assertThat(foundBeforeDelete).isTrue();
        
        // Soft delete
        savedTask.softDelete();
        taskJpaRepository.save(savedTask);
        entityManager.flush();
        entityManager.clear();
        
        // Verify task is NOT found by query
        boolean foundAfterDelete = taskJpaRepository.findByNameAndDeletedFalse("Filterable Task").isPresent();

        assertThat(foundAfterDelete).isFalse();
    }

    @Test
    @Order(4)
    @DisplayName("✅ Verify Name Reuse: can create task with same name after soft delete")
    void verifyNameReuseAfterSoftDelete() {
        // Create first task
        TaskEntity task1 = new TaskEntity();
        task1.setName("Reusable Name Test");
        task1.setDescription("First task");
        task1.setStatus(TaskStatus.PENDING);
        
        TaskEntity savedTask1 = taskJpaRepository.save(task1);
        entityManager.flush();
        
        // Soft delete first task
        savedTask1.softDelete();
        taskJpaRepository.save(savedTask1);
        entityManager.flush();
        
        // Create second task with same name
        TaskEntity task2 = new TaskEntity();
        task2.setName("Reusable Name Test");
        task2.setDescription("Second task");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        
        TaskEntity savedTask2 = taskJpaRepository.save(task2);
        entityManager.flush();
        
        assertThat(savedTask2.getId()).isNotEqualTo(savedTask1.getId());
        assertThat(savedTask2.getName()).isEqualTo("Reusable Name Test");
    }
}
