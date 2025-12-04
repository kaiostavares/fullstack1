package br.com.jtech.tasklist.adapters.output.persistence.repositories.jpa;

import br.com.jtech.tasklist.adapters.output.persistence.entities.TaskEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskJpaRepository extends BaseJpaRepository<TaskEntity> {
    
    @Query("SELECT t FROM TaskEntity t WHERE LOWER(t.name) = LOWER(:name) AND t.deleted = false")
    Optional<TaskEntity> findByNameAndDeletedFalse(@Param("name") String name);
}
