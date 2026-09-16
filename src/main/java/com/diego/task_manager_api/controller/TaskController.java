package com.diego.task_manager_api.controller;

import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }
    /**
     * Crea una actividad
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid CreateTaskRequest taskRequest){
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        URI location = URI.create("/tasks/"  + taskResponse.getId());
        return ResponseEntity
                .created(location)
                .body(taskResponse);
    }

    // Cuando se hace una petición GET a /tasks/{id}, se obtiene el ID de la URL
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
    /**
     * Implementar borrado tarea según su id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Crear Endpoint en el Controller
     */

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @RequestBody
            @Valid
            UpdateTaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }
}