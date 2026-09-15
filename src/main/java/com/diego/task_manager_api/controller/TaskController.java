package com.diego.task_manager_api.controller;

import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.entity.Task;
import com.diego.task_manager_api.exception.TaskNotFoundException;
import com.diego.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador encargado de recibir las peticiones HTTP
 * relacionadas con las tareas.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    // Servicio que contiene la lógica de negocio.
    private final TaskService taskService;

    /**
     * Spring inyecta automáticamente el TaskService.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Devuelve todas las tareas almacenadas.
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    /**
     * Crea una actividad
     */
    @PostMapping
    public TaskResponse createTask(@RequestBody @Valid CreateTaskRequest taskRequest){
        return taskService.createTask(taskRequest);
    }

    // Cuando se hace una petición GET a /tasks/{id}, se obtiene el ID de la URL
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
    /**
     * Implementar borrado tarea según su id
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /**
     * Crear Endpoint en el Controller
     */

    @PutMapping("/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @RequestBody
            @Valid
            UpdateTaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }
}