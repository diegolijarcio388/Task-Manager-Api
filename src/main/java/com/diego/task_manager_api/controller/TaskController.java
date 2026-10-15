package com.diego.task_manager_api.controller;

import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diego.task_manager_api.dto.PagedResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;


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
    public PagedResponse<TaskResponse> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") 
            @Min(value = 0, message = "La página no puede ser negativa")
            int page,
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "El tamaño de página debe ser como mínimo 1")
            @Max(value = 100, message = "El tamaño de página no puede superar 100")
            int size) {

        return taskService.getAllTasks(completed, search, page, size);
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