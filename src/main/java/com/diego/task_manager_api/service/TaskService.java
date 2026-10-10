package com.diego.task_manager_api.service;
import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.entity.Task;
import com.diego.task_manager_api.exception.TaskNotFoundException;
import com.diego.task_manager_api.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

/**
 * Servicio que contiene la lógica de negocio
 * relacionada con las tareas.
 */
@Service
public class TaskService {

    // Repositorio que accede a la base de datos.

    private final TaskRepository taskRepository;

    /**
     * Spring inyecta automáticamente el TaskRepository.
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Obtiene todas las tareas desde la base de datos
     * encontrando título ignorando mayúsculas y minúsculas
     *
     */
    public List <TaskResponse> getAllTasks(Boolean completed, String search, int page, int size){
        List <Task> tasks;
        Pageable pageable = PageRequest.of(page,size);
        if (completed != null && search != null) {
            tasks = taskRepository.findByCompletedAndTitleContainingIgnoreCase(completed,search, pageable)
                    .getContent();
        }
        else if (search != null){
            tasks = taskRepository.findByTitleContainingIgnoreCase(search, pageable).getContent();
        } else if (completed != null) {
            tasks = taskRepository.findByCompleted(completed, pageable).getContent();
        } else {
            tasks = taskRepository.findAll(pageable).getContent();
        }


        List<TaskResponse> list = new ArrayList<>();
        for (Task task : tasks){
            list.add(toResponse(task));
        }
        return list;
    }

    /**
     * Transformar directamente
     */
    private TaskResponse toResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt()
        );
    }
    private Task findTaskById(Long id){
        Task task =  taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("La tarea con el id: " + id + " no existe."));

        return task;
    }
    /**
     * Crea un Task
     */

    public TaskResponse createTask (CreateTaskRequest taskRequest){
        Task newTask = new Task();
        newTask.setTitle(taskRequest.getTitle());
        newTask.setCompleted(taskRequest.getCompleted());
        newTask.setDescription(taskRequest.getDescription());
        Task savedTask = taskRepository.save(newTask);
        return toResponse(savedTask);
    }

    /**
     * Encontrar tarea por Id
     */
    public TaskResponse getTaskById(Long id){
        Task task =  taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("La tarea con el id: " + id + " no existe."));
        return toResponse(task);
    }

    /**
     * Borrar tarea por id
     */

    public void deleteTask(Long id){
        findTaskById(id);
        taskRepository.deleteById(id);
    }

    /**
     *  Actualizar tarea
     */
    public TaskResponse updateTask(Long id, UpdateTaskRequest taskRequest) {
        Task existingTask = findTaskById(id);
        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setCompleted(taskRequest.getCompleted());
        existingTask.setDescription(taskRequest.getDescription());
        Task savedTask = taskRepository.save(existingTask);
        return toResponse(savedTask);
    }

}
