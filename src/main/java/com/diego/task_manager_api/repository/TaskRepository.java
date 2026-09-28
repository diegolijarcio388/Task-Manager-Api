package com.diego.task_manager_api.repository;

import com.diego.task_manager_api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio encargado de acceder a la base de datos
 * para realizar operaciones sobre la entidad Task.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{

    List<Task> findByCompleted(boolean completed);
}
