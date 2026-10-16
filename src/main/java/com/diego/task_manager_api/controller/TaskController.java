package com.diego.task_manager_api.controller;

import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import jakarta.websocket.OnError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diego.task_manager_api.dto.PagedResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    @Operation(
            summary = "Obtener tareas",
            description = "Devuelve una lista paginada de tareas, con filtros opcionales por estado y título."
    )
    public PagedResponse<TaskResponse> getAllTasks(
            @Parameter(
                    description = "Filtra las tareas por estado: true para completadas y false para pendientes"
            )
            @RequestParam(required = false) Boolean completed,
            @Parameter(
                    description = "Busca tareas cuyo título contenga el texto indicado, sin distinguir mayúsculas y minúsculas",
                    example = "Spring"
            )
            @RequestParam(required = false) String search,
            @Parameter(
                    description = "Número de página, comenzando desde 0",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") 
            @Min(value = 0, message = "La página no puede ser negativa")
            int page,
            @Parameter(
                    description = "Número de tareas por página (mínimo 1, máximo 100)",
                    example = "20"
            )
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
    @Operation(
            summary = "Crear una tarea",
            description = "Crea una nueva tarea a partir de los datos proporcionados y devuelve la tarea creada."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Tarea creada correctamente"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos de la tarea inválidos"
    )
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid CreateTaskRequest taskRequest){
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        URI location = URI.create("/tasks/"  + taskResponse.getId());
        return ResponseEntity
                .created(location)
                .body(taskResponse);
    }

    // Cuando se hace una petición GET a /tasks/{id}, se obtiene el ID de la URL
    @Operation(
            summary = "Obtener una tarea por ID",
            description = "Busca y devuelve una tarea a partir de su identificador."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tarea encontrada correctamente"
    )
    @ApiResponse(
            responseCode = "404",
            description = "No existe una tarea con el ID indicado"
    )
    @GetMapping("/{id}")
    public TaskResponse getTaskById(
            @Parameter(
                    description = "Identificador único de la tarea",
                    example = "12"
            )
            @PathVariable Long id) {
        return taskService.getTaskById(id);
    }
    /**
     * Implementar borrado tarea según su id
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una tarea",
            description = "Elimina una tarea a partir de su identificador"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Tarea eliminada correctamente"
    )
    @ApiResponse(
            responseCode = "404",
            description = "No existe una tarea con el ID indicado"
    )
    public ResponseEntity <Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Crear Endpoint en el Controller
     */
    @Operation(
            summary = "Actualizar una tarea",
            description = "Actualiza los datos de una tarea existente a partir de su identificador."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tarea actualizada correctamente"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos de la tarea inválidos"
    )
    @ApiResponse(
            responseCode = "404",
            description = "No existe una tarea con el ID indicado"
    )
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @RequestBody
            @Valid
            UpdateTaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }
}