package com.diego.task_manager_api.controller;

import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.entity.Task;
import com.diego.task_manager_api.exception.TaskNotFoundException;
import com.diego.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Task createTask(@RequestBody @Valid CreateTaskRequest taskRequest){
        return taskService.createTask(taskRequest);
    }

    // Cuando se hace una petición GET a /tasks/{id}, se obtiene el ID de la URL
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {

        // Buscamos la tarea mediante el Service.
        // El resultado puede contener un Task o estar vacío.
        Optional<Task> task = taskService.getTaskById(id);

        // Si la tarea existe, la devolvemos como respuesta.
        if (task.isPresent()) {
            return task.get();

            // Si no existe, devolvemos un error HTTP 404.
        } else {
            throw new TaskNotFoundException(
                    "No existe ninguna tarea con el id " + id
            );
        }
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
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /**
     * Redirigir un error con 404 Not Found
     */

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}