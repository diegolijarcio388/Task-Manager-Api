package com.diego.task_manager_api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios para crear una tarea.
 * Incluye las reglas de validación aplicadas a la petición.
 */
public class CreateTaskRequest {

    @NotBlank (message = "El título no debe estar vacío")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;
    @NotNull(message = "La tarea debe tener un estado")
    private Boolean completed;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
