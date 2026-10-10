package com.diego.task_manager_api;

import com.diego.task_manager_api.controller.TaskController;
import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.exception.TaskNotFoundException;
import com.diego.task_manager_api.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTaskById_WhenTaskExists_ReturnsOk() throws Exception {
        // ARRANGE
        TaskResponse taskResponse = new TaskResponse(
                7L,
                "Prueba HTTP 1",
                "Primera prueba",
                false,
                LocalDateTime.now()
        );

        when(taskService.getTaskById(7L))
                .thenReturn(taskResponse);

        // ACT + ASSERT
        mockMvc.perform(get("/tasks/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Prueba HTTP 1"))
                .andExpect(jsonPath("$.description").value("Primera prueba"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getTaskById_WhenTaskNotExists_ReturnsNotFound() throws Exception {
        // ARRANGE
        when(taskService.getTaskById(99L))
                .thenThrow(new TaskNotFoundException(
                        "La tarea con el id: 99 no existe."
                ));

        // ACT + ASSERT
        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail")
                        .value("La tarea con el id: 99 no existe."));
    }

    @Test
    void getAllTasks_WhenTasksExist_ReturnsOk() throws Exception {
        TaskResponse taskResponse1 = new TaskResponse(
                1L,
                "Primer TaskResponse",
                "Primero",
                true,
                LocalDateTime.now()
        );

        TaskResponse taskResponse2 = new TaskResponse(
                2L,
                "Segundo TaskResponse",
                "Segundo",
                true,
                LocalDateTime.now()
        );

        when(taskService.getAllTasks(null, null,0,20))
                .thenReturn(List.of(taskResponse1, taskResponse2));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Primer TaskResponse"))
                .andExpect(jsonPath("$[0].description")
                        .value("Primero"))
                .andExpect(jsonPath("$[1].title")
                        .value("Segundo TaskResponse"))
                .andExpect(jsonPath("$[1].description")
                        .value("Segundo"));
    }

    @Test
    void createTask_WhenRequestIsValid_ReturnsCreated() throws Exception {
        TaskResponse taskResponseDefault = new TaskResponse(
                7L,
                "Prueba HTTP 1",
                "Primera prueba",
                false,
                LocalDateTime.now()
        );

        String json = """
                {
                  "title": "Aprender MockMvc",
                  "description": "Crear el primer test de POST",
                  "completed": false
                }
                """;

        when(taskService.createTask(any(CreateTaskRequest.class)))
                .thenReturn(taskResponseDefault);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tasks/7"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Prueba HTTP 1"))
                .andExpect(jsonPath("$.description").value("Primera prueba"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createTask_WhenRequestIsInvalid_ReturnsBadRequest() throws Exception {
        String json = """
                {
                  "title": "",
                  "description": "Descripción válida",
                  "completed": false
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title")
                        .value("El título no debe estar vacío"))
                .andExpect(jsonPath("$.detail")
                        .value("Uno o más campos no son válidos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title")
                        .value("Error de validación"));
    }

    @Test
    void updateTask_WhenTaskExists_ReturnsOk() throws Exception {
        String json = """
                {
                  "title": "Tarea actualizada",
                  "description": "Descripción actualizada",
                  "completed": true
                }
                """;

        TaskResponse updatedTask = new TaskResponse(
                7L,
                "Tarea actualizada",
                "Descripción actualizada",
                true,
                LocalDateTime.now()
        );

        when(taskService.updateTask(
                eq(7L),
                any(UpdateTaskRequest.class)))
                .thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }
    @Test
    void updateTask_WhenTaskNotExists_ReturnsNotFound() throws Exception {
        // ARRANGE
        String json = """
            {
              "title": "Tarea actualizada",
              "description": "Descripción actualizada",
              "completed": true
            }
            """;

        when(taskService.updateTask(
                eq(7L),
                any(UpdateTaskRequest.class)))
                .thenThrow(new TaskNotFoundException(
                        "La tarea con el id 7 no existe"
                ));

        // ACT + ASSERT
        mockMvc.perform(put("/tasks/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("La tarea con el id 7 no existe"))
                .andExpect(jsonPath("$.status").value(404));
    }
    @Test
    void deleteTask_WhenTaskExists_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/tasks/7"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteTask_WhenTaskNotExists_ReturnsNotFound() throws Exception {
        doThrow(new TaskNotFoundException("La tarea con el id 7 no existe"))
                .when(taskService)
                .deleteTask(7L);
        mockMvc.perform(delete("/tasks/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("La tarea con el id 7 no existe")
                )
                .andExpect(jsonPath("$.status")
                        .value(404)
                );
    }

    @Test
    void createTask_WhenCompletedHasInvalidType_ReturnsBadRequest() throws Exception {
        String json = """
                {
                  "title": "Prueba tipo inválido",
                  "description": "Prueba para dar fallo en boolean",
                  "completed": "patata"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title")
                        .value("Petición no válida"))
                .andExpect(jsonPath("$.detail")
                        .value("El cuerpo de la petición no se puede interpretar"));
    }

    @Test
    void getAllTasks_WhenCompletedFilterIsTrue() throws Exception {
        // ARRANGE
        TaskResponse task1 = new TaskResponse(
                1L,
                "Aprender Spring",
                "Practicar filtros",
                true,
                LocalDateTime.now()
        );

        TaskResponse task2 = new TaskResponse(
                2L,
                "Hacer tests",
                "Probar el filtro",
                true,
                LocalDateTime.now()
        );

        when(taskService.getAllTasks(true, null,0,20))
                .thenReturn(List.of(task1, task2));

        // ACT + ASSERT
        mockMvc.perform(get("/tasks?completed=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title")
                        .value("Aprender Spring"))
                .andExpect(jsonPath("$[0].completed")
                        .value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title")
                        .value("Hacer tests"))
                .andExpect(jsonPath("$[1].completed")
                        .value(true));

        verify(taskService).getAllTasks(true, null,0,20);
    }

    @Test
    void getAllTasks_WhenSearchIsProvided() throws Exception {
        // ARRANGE
        TaskResponse taskResponse = new TaskResponse(
                1L,
                "Aprender Spring Boot",
                "Practicar búsquedas",
                false,
                LocalDateTime.now()
        );

        when(taskService.getAllTasks(null, "spring", 0,20))
                .thenReturn(List.of(taskResponse));

        // ACT + ASSERT
        mockMvc.perform(get("/tasks?search=spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title")
                        .value("Aprender Spring Boot"))
                .andExpect(jsonPath("$[0].description")
                        .value("Practicar búsquedas"))
                .andExpect(jsonPath("$[0].completed")
                        .value(false));

        verify(taskService).getAllTasks(null, "spring",0,20);
    }

    @Test
    void getAllTasks_WhenCompletedAndSearchAreProvided_UsesCombinedFilter()
            throws Exception {

        // ARRANGE
        TaskResponse taskResponse = new TaskResponse(
                1L,
                "Aprender Spring",
                "Practicar filtros combinados",
                true,
                LocalDateTime.now()
        );

        when(taskService.getAllTasks(true, "spring",0,20))
                .thenReturn(List.of(taskResponse));

        // ACT + ASSERT
        mockMvc.perform(get("/tasks?completed=true&search=spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title")
                        .value("Aprender Spring"))
                .andExpect(jsonPath("$[0].description")
                        .value("Practicar filtros combinados"))
                .andExpect(jsonPath("$[0].completed")
                        .value(true));

        verify(taskService).getAllTasks(true, "spring",0,20);
    }
}