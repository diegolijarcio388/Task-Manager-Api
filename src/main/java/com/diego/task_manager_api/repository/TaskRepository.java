package com.diego.task_manager_api.repository;

import com.diego.task_manager_api.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Repositorio encargado de acceder a la base de datos
 * para realizar operaciones sobre la entidad Task.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{

    Page<Task> findByCompleted(boolean completed, Pageable pageable);

    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Task> findByCompletedAndTitleContainingIgnoreCase(
            Boolean completed,
            String search,
            Pageable pageable
    );
}
